#多线程之间通讯#
1. 多线程之间通讯
    多个线程在操作同一个资源，但是操作的动作不同

2. wait()、notify()区别
    1. 因为涉及到对象锁,他们必须都放在synchronized中来使用. Wait、Notify一定要在synchronized里面进行使用。
    2. wait必须暂定当前正在执行的线程,并释放资源锁,让其他线程可以有机会运行
    3. notify/notifyall: 唤醒因锁池中的线程,使之运行
    
3. wait()、sleep()区别
    1. sleep()方法属于Thread类中的;wait()方法属于Object类中的。
    2. sleep()方法：导致了程序暂停执行指定的时间，让出cpu该其他线程，但监控状态依然保持者，当指定的时间到了又会自动恢复运行状态。在调用sleep()方法的过程中，线程不会释放对象锁。
       wait()方法：线程会放弃对象锁，进入等待此对象的等待锁定池；只有针对此对象调用notify()方法后本线程才进入对象锁定池准备,获取对象锁进入运行状态。

4. Lock锁
    并发包中新增了 Lock 接口(以及相关实现类)用来实现锁功能。
    Lock 接口提供了与 synchronized 关键字类似的同步功能，但需要在使用时手动获取锁和释放锁。
    （* 实现机制：CAS操作，使用AQS
    
    Lock lock  = new ReentrantLock();
    lock.lock();
    try{
        //可能会出现线程安全的操作
    }finally{
        //一定在finally中释放锁
        //也不能把获取锁在try中进行，因为有可能在获取锁的时候抛出异常
        lock.unlock();
    }
    
5. Lock与synchronized 关键字的区别
    0. synchronized(悲观锁)；Lock(乐观锁，读写锁接口)
    1. synchronized(内置锁 java关键字,jvm层面)；Lock(是一个类)
    2. synchronized(A.以获取锁的线程执行完同步代码，释放锁 B.线程执行发生异常，jvm会让线程释放锁)
       Lock(在finally中必须手动释放锁，不然容易造成线程死锁)
    3. synchronized(假设A线程获得锁，B线程等待。如果A线程阻塞，B线程会一直等待)
       Lock(分情况而定，Lock有多个锁获取的方式,可尝试获得锁，线程可以不用一直等待)，比较灵活
    4. synchronized(锁状态无法判断),Lock(锁状态可以判断)
    5. synchronized(锁类型-可重入,不可中断,非公平),Lock(锁类型-可重入-可判断-可公平(两者皆可))
    6. 性能：synchronized(少量同步),Lock(大量同步)
    7. Lock(lockInterruptibly() 如果线程在获取锁的阶段进入了等待，那么可以中断此线程，先去做别的事)
        
    * 在非常复杂的同步应用中，请考虑使用ReentrantLock
        1.某个线程在等待一个锁的控制权的这段时间 *需要中断*
        2.需要 *分开处理一些wait-notify*，ReentrantLock里面的Condition应用，能够控制notify哪个线程
        3.具有 *公平锁功能* ，每个到来的线程都将排队等候

6. Condition用法
    Condition的功能类似于在传统的线程技术中的,Object.wait()和Object.notify()的功能。
     
    Condition condition = lock.newCondition();
    res.condition.await();   类似wait
    res.Condition.Signal();  类似notify
    
    