#锁的深入化#
1. 重入锁(又名 递归锁)
    同一线程外层函数获得锁之后，内层递归函数仍然有获取该锁的代码，但不受影响。
    且内层函数可以获取外层函数锁的状态，如果已经获取锁/释放锁 则不在进行对应操作（防止死锁）
    
    Synchronized重入锁:(重量级)(悲观锁)
    ReentrantLock重入锁:(轻量级)(悲观锁)
    
2. 读写锁（JUC包）
    两个线程同时读一个资源没有任何问题，允许多个线程能在同时读取共享资源。
    有一个线程想去写这些共享资源，就不应该再有其它线程对该资源进行读或写。
    即：读-读能共存，读-写不能共存，写-写不能共存
    ReentrantReadWriteLock

3. 悲观锁、乐观锁
    乐观锁:
        不会上锁。如果满足条件则操作，如果不满足条件则重试。
        在更新时会判断其他线程在这之前有没有对数据进行修改，一般会使用版本号机制或CAS操作实现。
        - version方式：
            在数据表中加上数据版本号version字段，表示数据被修改的次数，当数据被修改时，version值会加一。
            当线程A更新数据值时，在读取数据同时也会读取version值，在提交更新时，若刚才读取到的version值为当前数据库中的version值相等时才更新，否则重试更新操作，直到更新成功。
            * 核心SQL语句
            update table set x=x+1, version=version+1 where id=#{id} and version=#{version};
        - CAS操作方式：即compare and swap 或者 compare and set，涉及到三个操作数，数据所在的 内存值，预期值，新值。
            当需要更新时，判断当前内存值与之前取到的值是否相等，若相等，则用新值更新，若失败则重试，一般情况下是一个自旋操作，即不断的重试。
    悲观锁:
        总是假设最坏的情况，每次取数据时都认为其他线程会修改，都会加锁（读锁、写锁、行锁等），当其他线程想要访问数据时，都需要阻塞挂起。
        可以依靠数据库实现，如行锁、读锁和写锁等，都是在操作之前加锁，在Java中，synchronized的思想也是悲观锁。
    
4. 原子类
    Atomic:(乐观锁)使用CAS操作
        java.util.concurrent.atomic包;原子类的小工具包,支持在单个变量上解除锁的线程安全编程
        原子变量类相当于一种泛化的volatile变量，能够支持原子的和有条件的读-改-写操作。
        
        Atomic包里的类基本都是使用Unsafe实现的包装类。
        在Atomic包里一共有12个类，四种原子更新方式:
        1.原子更新基本类型
            AtomicBoolean：原子更新布尔类型。
            AtomicInteger：原子更新整型。
            AtomicLong：原子更新长整型。
                如果是更新其他基本类型（char、float和double等），会把对应的类型转换为整型 再使用CAS操作）
            AtomicInteger表示一个int类型的值，并提供了get和set方法，这些Volatile类型的int变量在读取和写入上有着相同的内存语义。
                它还提供了一个原子的compareAndSet方法（如果该方法成功执行，那么将实现与读取/写入一个volatile变量相同的内存效果），以及原子的添加、递增和递减等方法。
                AtomicInteger表面上非常像一个扩展的Counter类，但在发生竞争的情况下能提供更高的可伸缩性，因为它直接利用了硬件对并发的支持。

        2.原子更新数组
            AtomicIntegerArray：原子更新整型数组里的元素。
            AtomicLongArray：原子更新长整型数组里的元素。
            AtomicReferenceArray：原子更新引用类型数组里的元素。
                ps:AtomicIntegerArray类：数组value通过构造方法传递进去，然后会将当前数组复制一份，所以当AtomicIntegerArray对内部的数组元素进行修改时，不会影响到传入的数组。
        
        3.原子更新引用
            AtomicReference：原子更新引用类型。
            AtomicReferenceFieldUpdater：原子更新引用类型里的字段。
            AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子的更新一个布尔类型的标记位和引用类型。构造方法是AtomicMarkableReference(V initialRef, boolean initialMark)
                ps:原子更新基本类型的AtomicInteger，只能更新一个变量；如果要原子的更新多个变量，就需要使用这个原子更新引用类型提供的类。
        
        4.原子更新字段
            AtomicIntegerFieldUpdater：原子更新整型的字段的更新器。
            AtomicLongFieldUpdater：原子更新长整型字段的更新器。
            *AtomicStampedReference：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更数据和数据的 *版本号* ，可以解决使用CAS进行原子更新时，可能出现的ABA问题。
                ps:原子更新字段类都是抽象类，每次使用都时候必须使用静态方法newUpdater创建一个更新器。原子更新类的 *字段的必须使用public volatile修饰符* 。

5. 自旋锁:属于乐观锁。线程一直是running(加锁 --> 解锁)，死循环检测锁的标识位，机制不复杂 （CAS
   互斥锁:属于悲观锁。互斥等待、阻塞。线程会从sleep(加锁) --> running(解锁)，过程中有上下文的切换，cpu的抢占，信号的发送等开销
   
6. 公平锁:先进先出
   非公平锁:在等待锁的过程中，如果有任意新的线程妄图获取锁，都是有很大几率直接获取到锁的（即 不排队直接拿，失败再说

   都是基于锁内部维护的一个双向链表，表节点node的值 就是 每一个请求当前锁的线程。
    
to_study
7. Disruptor（高性能的异步处理框架、并发编程框架 
   采用 CAS无锁机制（乐观锁）、观察者模式、事件监听

8. 分布式锁
    如果想在不同的jvm中保证数据同步，使用分布式锁技术。
    缓存实现、 * Zookeeper分布式锁

-------------------------------------------------

- CAS无锁模式：Compare and Swap，即比较再交换。
    好处：
        第一，高并发下，性能优于有锁程序
        第二，不存在死锁情况
    CAS缺点：
        ABA问题：解决方法（原子引用类AtomicStampedReference，它可以通过控制变量值的版本来保证CAS的正确性。      
    
    *CAS算法的过程：
        内存值，预期值，新值
            v = 需要更新变量 主内存
            e = 期望值 本地内存
            n = 新值
            if(v==e){//主内存值与本地内存值一致
                //没有被修改过
                v=n;
            }else if(v!=e){//主内存值与本地内存值不一致
                //已经被修改
            }    
        当多个线程同时使用CAS操作一个变量时，只有一个会胜出，并成功更新，其余均会失败。
        失败的线程不会被挂起，仅是被告知失败，并且允许再次尝试，当然也允许失败的线程放弃操作。
           
- AQS：队列同步器; JUC并发包中的核心基础组件。
     构建锁或者其他同步组件的基础框架（如ReentrantLock、ReentrantReadWriteLock、Semaphore等）
    
    实现同步器时涉及到的大量细节问题（例如获取同步状态、FIFO同步队列）能够极大地减少实现工作，且不必处理在多个位置上发生的竞争问题。
    在基于AQS构建的同步器中，只能在一个时刻发生阻塞，从而降低上下文切换的开销，提高了吞吐量。
    同时在设计AQS时充分考虑了可伸缩行，因此J.U.C中所有基于AQS构建的同步器均可以获得这个优势。
    
    采用模板方法设计模式，主要使用方式是继承，子类通过继承同步器并实现它的抽象方法来管理同步状态
    AQS提供了大量的模板方法来实现同步，主要是分为三类：
        独占式获取和释放同步状态
        共享式获取和释放同步状态
        查询同步队列中的等待线程情况
    
    AQS使用一个int类型的成员变量state来表示同步状态
        当state>0时表示已经获取了锁
        当state=0时表示释放了锁。
        它提供了三个方法 getState()、setState(int newState)、compareAndSetState(int expect,int update)）来对同步状态state进行操作，当然AQS可以确保对state的操作是安全的。
    
    AQS通过CLH同步队列(FIFO双向队列，AQS依赖它来完成同步状态的管理)来完成资源获取线程的排队工作，
        当前线程如果获取同步状态失败时，AQS则会将当前线程已经等待状态等信息构造成一个节点（Node）并将其加入到CLH同步队列，同时会阻塞当前线程。
        当同步状态释放时，会把首节点唤醒（公平锁），使其再次尝试获取同步状态。
        (一个节点表示一个线程，它保存着线程的引用(thread)、状态(waitStatus)、前驱节点(prev)、后继节点(next))
        入列：tail指向新节点、新节点的prev指向当前最后的节点
        * AQS通过“死循环”的方式来保证节点可以正确添加，只有成功添加后，当前线程才会从该方法返回，否则会一直执行下去。
        出列：首节点的线程释放同步状态后，将会唤醒它的后继节点（next），而后继节点将会在获取同步状态成功时将自己设置为首节点（不需要使用CAS
        
    java并发包提供的加锁模式分为独占锁和共享锁
        独占锁：每次只能有一个线程能持有锁，（ReentrantLock就是以独占方式实现的互斥锁）
        共享锁：允许多个线程同时获取锁，并发访问共享资源（ReadWriteLock）

        AQS的内部类Node定义了两个常量SHARED和EXCLUSIVE，他们分别标识 AQS队列中等待线程的锁获取模式。
        AQS提供了独占锁和共享锁必须实现的方法，
            具有独占锁功能的子类，它必须实现tryAcquire、tryRelease、isHeldExclusively等；
            共享锁功能的子类，必须实现tryAcquireShared和tryReleaseShared等方法，带有Shared后缀的方法都是支持共享锁加锁的语义。
                Semaphore是一种共享锁，ReentrantLock是一种独占锁。
        AQS的ConditionObject只能与ReentrantLock(独占锁)一起使用，它是为了支持条件队列的锁更方便。
        （ConditionObject的signal和await方法都是基于独占锁的，如果线程非锁的独占线程，则会抛出IllegalMonitorStateException
       