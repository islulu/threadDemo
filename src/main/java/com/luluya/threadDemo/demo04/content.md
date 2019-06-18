#线程池原理分析#
0.队列
    常用的队列主要有以下两种：
    （当然通过不同的实现方式，还可以延伸出很多不同类型的队列）
        先进先出（FIFO）：先插入的队列的元素也最先出队列。从某种程度上来说这种队列也体现了一种公平性。
        后进先出（LIFO）：后插入队列的元素最先出队列，这种队列优先处理最近发生的事件。
        
1. 并发包(类位于java.util.concurrent包下)
    (计数器)CountDownLatch:（通过AQS里面的共享锁来实现
        eg：有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
        
        CountDownLatch是通过一个计数器来实现的，#计数器的初始值为线程的数量#。
        每当一个线程完成了自己的任务后，计数器的值就会减1。当计数器值到达0时，它表示所有的线程已经完成了任务，然后在闭锁上等待的线程就可以恢复执行任务。
        
        public void await() throws InterruptedException { };   //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
        public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };  //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
        public void countDown() { };  //将count值减1
        
        CountDownLatch无法进行重复使用
        
    (屏障)CyclicBarrier:
        构造器：
            - public CyclicBarrier(int parties) { }
            CyclicBarrier初始化时规定一个数目，然后计算调用了CyclicBarrier.await()进入等待的线程数。
                当线程数达到了这个数目时，所有进入等待状态的线程被唤醒并继续。 
            - public CyclicBarrier(int parties, Runnable barrierAction) { }
            CyclicBarrier初始时还可带一个Runnable的参数，此Runnable任务在CyclicBarrier的数目达到后，所有其它线程被唤醒前被执行。
            Runnable方法会从所有线程中选择一个线程进行执行
        
        await方法：
            - 【常用】public int await() throws InterruptedException, BrokenBarrierException { };
            用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务
            - public int await(long timeout, TimeUnit unit)throws InterruptedException,BrokenBarrierException,TimeoutException { };
            让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。
            
        初次的4个线程越过barrier状态后，又可以用来进行新一轮的使用
         
    (计数信号量)Semaphore:
        Semaphore可以控制同时访问的线程个数，通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
        Semaphore是一种基于计数的信号量。它可以设定一个阈值，基于此，多个线程竞争获取许可信号，做自己的申请后归还，超过阈值后，线程申请许可信号将会被阻塞
        Semaphore可以用来构建一些对象池，资源池之类的，比如数据库连接池，也可以创建计数为1的Semaphore，将其作为一种类似互斥锁的机制，这也叫二元信号量，表示两种互斥状态。
        
        构造器：
            public Semaphore(int permits) { sync = new NonfairSync(permits); }
            //参数permits表示许可数目，即同时可以允许多少线程进行访问
            public Semaphore(int permits, boolean fair) { sync = (fair)? new FairSync(permits) : new NonfairSync(permits); }
            //这个多了一个参数fair表示是否是公平的，即等待时间越久的越先获取许可
        重用方法：
            availablePermits函数用来获取当前可用的资源数量
            会被阻塞：
                acquire()：用来获取一个许可，若无许可能够获得，则会一直等待，直到获得许可。
                release()：用来释放许可。注意，在释放许可之前，必须先获获得许可。
                --------------
                public void acquire() throws InterruptedException {  }              //获取一个许可
                public void acquire(int permits) throws InterruptedException { }    //获取permits个许可
                public void release() { }                                           //释放一个许可
                public void release(int permits) { }                                //释放permits个许可
            想立即得到执行结果：
                public boolean tryAcquire() { };                                                                     //尝试获取一个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
                public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException { };              //尝试获取一个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
                public boolean tryAcquire(int permits) { };                                                          //尝试获取permits个许可，若获取成功，则立即返回true，若获取失败，则立即返回false
                public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException { }; //尝试获取permits个许可，若在指定的时间内获取成功，则立即返回true，否则则立即返回false
        
        // 创建一个计数阈值为5的信号量对象
        // 只能5个线程同时访问
        Semaphore semp = new Semaphore(5);
        try {
            // 申请许可
            semp.acquire();
            try {
                // 业务逻辑
            } catch (Exception e) {

            } finally {
                // 释放许可
                semp.release();
            }
        } catch (InterruptedException e) {

        }
        
    总结：
    　　1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：
            CountDownLatch：一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
            CyclicBarrier：一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
            
            CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
    　　
        2）Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。

2. 并发队列
    在并发队列上JDK提供了两套实现(无论哪种都继承自Queue)
    一个是以ConcurrentLinkedQueue为代表的高性能队列非阻塞
    一个是以BlockingQueue接口为代表的阻塞队列

    阻塞队列与非阻塞队:
        阻塞队列与普通队列的区别在于:
            当队列是空时，从队列中获取元素的操作将会被阻塞，直到其他的线程往空的队列插入新的元素。
            当队列是满时，往队列里添加元素的操作会被阻塞，直到其他的线程使队列重新变得空闲起来（从队列中移除一个或者多个元素，或者完全清空队列）
            1.ArrayDeque, （数组双端队列） 
            2.PriorityQueue, （优先级队列） 
            3.ConcurrentLinkedQueue, （基于链表的并发队列） 
            ----------------
            4.DelayQueue, （延期阻塞队列）（阻塞队列实现了BlockingQueue接口） 
            5.ArrayBlockingQueue: 基于数组的并发阻塞队列 
            6.LinkedBlockingQueue: 基于链表的FIFO阻塞队列
            7.LinkedBlockingDeque: 基于链表的FIFO双端阻塞队列
            8.LinkedTransferQueue: 基于链表结构组成的无界阻塞队列
            9.PriorityBlockingQueue: 带优先级的无界阻塞队列
            10.SynchronousQueue: 并发同步阻塞队列
    
    - 非阻塞队列:
        ConcurrentLinkedQueue:(CAS操作)
            基于链接节点的无界线程安全队列，队列的元素遵循先进先出的原则(不允许null元素)
            采用了有效的“无等待(wait-free)”算法
            把元素放入到队列的线程的优先级 高于 对元素的访问和移除的线程
            
            对公共集合的共享访问就可以工作得很好。收集关于队列大小的信息会很慢,需要遍历队列
            
            是一个适用于高并发场景下的队列,通过无锁的方式实现了高并发状态下的高性能
            通常ConcurrentLinkedQueue性能好于BlockingQueue
            重要方法:
                add()/offer():都是加入元素的方法(在ConcurrentLinkedQueue中这俩个方法没有任何区别)
                poll()/peek():都是取头元素节点,区别在于前者会删除元素,后者不会
            
            * ConcurrentLinkedQueue的.size()是要遍历一遍集合的,很慢的;尽量要避免用size,如果判断队列是否为空最好用isEmpty()而不是用size来判断
            eg: queue.add(obj);
            使用了ConcurrentLinkedQueue直接使用它提供的函数(add/poll)不需要做任何同步;如果是非原子操作需要自己同步
            eg: synchronized(queue){
                    if(!queue.isEmpty()) {
                        queue.poll(obj);
                    }
                }
    
    - 阻塞队列：
        （在多线程领域：所谓阻塞，在某些情况下会挂起线程（即阻塞），一旦条件满足，被挂起的线程又会自动被唤醒）

        BlockingQueue：位于java.util.concurrent包中(在Java5版本开始提供)是线程安全的
            一个支持两个附加操作的队列。
            这两个附加的操作是：
                在队列空时，获取元素的线程会等待队列变为非空。
                当队列满时，存储元素的线程会等待队列可用。

            阻塞队列常用于生产者和消费者的场景：生产者是往队列里添加元素的线程，消费者是从队列里拿元素的线程。
            阻塞队列就是生产者存放元素的容器，而消费者也只从容器里拿元素。

            被阻塞的情况主要有如下两种：
                1. 当队列满了的时候进行入队列操作（除非有另一个线程做了出队列操作
                2. 当队列空了的时候进行出队列操作（除非有另一个线程进行了入队列操作

            多消费者与多生产者，消费生产速度不一致导致需要开发者手动调整消费生产的相关细节且兼顾效率和线程安全则复杂度不低
            JUC则推出BlockingQueue

            阻塞队列提供了四种处理方法:
                方法\处理方式	 抛出异常	    返回特殊值	一直阻塞	  超时退出
                插入方法	     add(e)	    offer(e)	put(e)	  offer(e,time,unit)
                移除方法	     remove()	poll()	    take()	  poll(time,unit)
                检查方法	     element()	peek()	    不可用	  不可用

        - BlockingQueue家庭中的所有成员，包括他们各自的功能以及常见使用场景：
                有边界即 它的容量是有限的，其初始化时必须指定其容量大小，容量大小一旦指定就不可改变。
        ------------------------------------
        阻塞队列实现了BlockingQueue接口

        5.ArrayBlockingQueue: 基于数组的并发阻塞队列 
        6.LinkedBlockingQueue: 基于链表的FIFO阻塞队列
        7.LinkedBlockingDeque: 基于链表的FIFO双端阻塞队列
        8.LinkedTransferQueue: 基于链表结构组成的无界阻塞队列
        9.PriorityBlockingQueue: 带优先级的无界阻塞队列
        1.SynchronousQueue: 并发同步阻塞队列
        4.DelayQueue: 支持延时获取元素的无界阻塞队列
        
        ArrayBlockingQueue：基于数组的FIFO并发阻塞队列
            不保证访问者公平访问队列(公平访问队列：阻塞的所有生产者线程或消费者线程 当队列可用时按照阻塞的先后顺序访问队列。 会降低吞吐量。使用可重入锁实现)
        
        LinkedBlockingQueue：基于链表的FIFO阻塞队列
            队列大小的配置是可选的
                如果我们初始化时指定一个大小，它就是有边界的
                如果不指定，它就是无边界的。说是无边界，其实是采用了默认大小为Integer.MAX_VALUE的容量
        
        LinkedBlockingDeque: 基于链表的FIFO双端阻塞队列
        
        LinkedTransferQueue: 基于链表结构组成的无界阻塞队列
            相对于其他阻塞队列，多了tryTransfer和transfer方法
            transfer:如果当前有消费者正在等待接收元素(消费者使用take()方法或带时间限制的poll()方法时)
                  可把生产者传入的元素立刻transfer(传输)给消费者。
                  如果没有消费者在等待接收元素，将元素存放在队列的tail节点，并等到该元素被消费者消费了才返回。
            tryTransfer:用来试探下生产者传入的元素是否能直接传给消费者
                  如果没有消费者等待接收元素，则返回 false。立即返回结果！
          
        PriorityBlockingQueue：带优先级的无界阻塞队列(允许插入null对象)（底层是在做扩容的）
            排序规则和java.util.PriorityQueue一样,
                1。默认情况下元素采取自然顺序排列,元素按照升序排列
                2。也可通过比较器comparator来指定元素的排序规则
                3。可从PriorityBlockingQueue获得一个迭代器Iterator，但这个迭代器并不保证按照优先级顺序进行迭代。
        
        SynchronousQueue：
            内部仅允许容纳一个元素。当一个线程插入一个元素后会被阻塞，除非这个元素被另一个线程消费。
            
            不存储元素的阻塞队列。每一个put操作必须等待一个take操作，否则不能继续添加元素。
            适合于传递性场景, 比如在一个线程中使用的数据，传递给另外一个线程使用，SynchronousQueue的吞吐量高于LinkedBlockingQueue和ArrayBlockingQueue。
        
        DelayQueue：支持延时获取元素的无界阻塞队列
            使用PriorityQueue来实现，队列中的元素必须实现Delayed接口(其必须实现compareTo来指定元素的顺序)，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。
            应用场景：
                缓存系统的设计：用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，表示缓存有效期到了。
                定时任务调度：用DelayQueue保存当天将会执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，从比如 TimerQueue就是使用DelayQueue实现的

3. 线程池：
    （使用的是阻塞队列，因为核心线程执行需要耗时，所以需要用阻塞队列（用非阻塞队列，当核心线程未完成则会直接创建线程了）
    好处：
    a,降低资源消耗：通过重复利用已创建的线程 降低线程创建销毁造成的消耗
    b,提高响应速度：任务达到时，不需要等待线程创建即可立即执行
    c,提高线程管理性：无限制的创建线程会 消耗系统资源 降低系统稳定性，使用线程池可以进行统一分配、调优、监控
        * 要做到合理利用
    
    原理：
      Executors ThreadPoolExecutor
        corePoolSize：线程池核心线程数。最大运行线程数。当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，会把到达的任务放到缓存队列当中
        maximumPoolSize：线程池最大线程数，最大创建线程数。表示在线程池中最多能创建多少个线程】
        核心线程数<=最大线程数
        
        keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。
        unit：keepAliveTime的时间单位，有7种取值。
       
    四种创建方式：
        newCachedThreadPool：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        newFixedThreadPool：创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        newScheduledThreadPool：创建一个定长线程池，支持定时及周期性任务执行。
        newSingleThreadExecutor：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

    原理剖析：
        提交一个任务到线程池中，线程池的处理流程如下：
        1、判断线程池里的核心线程是否都在执行任务
                如果不是（核心线程空闲或者还有核心线程没有被创建）则创建一个新的工作线程来执行任务
                如果核心线程都在执行任务，则进入下个流程
        2、线程池判断工作队列是否已满
                如果工作队列没有满，则将新提交的任务存储在这个工作队列里
                如果工作队列满了，则进入下个流程
        3、判断线程池里的线程是否都处于工作状态
                如果没有，则创建一个新的工作线程来执行任务
                如果已经满了，则交给饱和策略来处理这个任务
        
    Executor框架是一个根据一组执行策略调用、调度、执行和控制的异步任务的框架。
    Executors为Executor、ExecutorService、ScheduledExecutorService、ThreadFactory 和 Callable 类提供了一些工具方法。
    Executors可以用于方便的创建线程池。
    
    execute/submit区别：
        execute:(无对应的调用结果)
            只能提交一个Runnable的对象，且该方法的返回值是void;
            可以设置一些变量来获取到线程的运行结果;
            当线程的执行过程中抛出了异常通常来说主线程也无法获取到异常的信息的，只有通过ThreadFactory主动设置线程的异常处理类才能感知到提交的线程中的异常信息。
        submit:(会有对应的返回结果)
            <T> Future<T> submit(Callable<T> task);提交一个实现了Callable接口的对象
            Future<?> submit(Runnable task);提交一个Runnable接口的对象，当调用get方法的时候，如果线程执行成功会直接返回null，如果线程执行异常会返回异常的信息
            <T> Future<T> submit(Runnable task, T result);当线程正常结束的时候调用Future的get方法会返回result对象，当线程抛出异常的时候会获取到对应的异常的信息。
            
4. 自定义线程线程池
    如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
    
    如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，
        若添加成功，则该任务会等待空闲线程将其取出去执行；
        若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
    
    如果队列已经满了，则在总线程数不大于maximumPoolSize的前提下，则创建新的线程
    
    如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
    
    如果线程池中的线程数量大于corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；
    如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。

    任务拒绝策略：
        AbortPolicy:直接抛出异常(默认)
        CallerRunsPolicy:线程池满则直接执行当前任务
        DiscardOldestPolicy:当任务添加到线程池中被拒绝时，线程池会放弃等待队列中最旧的未处理任务，然后将被拒绝的任务添加到等待队列中。
        DiscardPolicy:直接丢弃要执行的任务

5. 如何合理的设置线程池大小
    1.	任务的性质：CPU密集型任务、IO密集型任务、混合型任务。
    2.	任务的优先级：高、中、低。
    3.	任务的执行时间：长、中、短。
    4.	任务的依赖性：是否依赖其他系统资源，如数据库连接等。
    
    CPU密集型：任务可以少配置线程数，大概和机器的cpu核数相当，这样可以使得每个线程都在执行任务
    IO密集型：大部分线程都阻塞，故需要多配置线程数，2*cpu核数
    
    最大线程数目 = （线程等待时间与线程CPU时间之比 + 1）* CPU数目
    线程等待时间所占比例越高，需要越多线程。线程CPU时间所占比例越高，需要越少线程。 
