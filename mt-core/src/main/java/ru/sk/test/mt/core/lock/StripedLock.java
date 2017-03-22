package ru.sk.test.mt.core.lock;

import jersey.repackaged.com.google.common.base.Preconditions;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Sergey_Karnaukhov on 22.03.2017
 */
public class StripedLock {

    private final NumberedLock[] locks;

    private static class NumberedLock {
        private final long id;
        private final ReentrantLock lock;

        public NumberedLock(long id) {
            this.id = id;
            this.lock = new ReentrantLock();
        }
    }


    /**
     * Default ctor, creates 16 locks
     */
    public StripedLock() {
        this(4);
    }

    /**
     * Creates array of locks, size of array may be any from set {2, 4, 8, 16, 32, 64}
     *
     * @param storagePower size of array will be equal to <code>Math.pow(2, storagePower)</code>
     */
    public StripedLock(int storagePower) {
        if (!(storagePower >= 1 && storagePower <= 6)) {
            throw new IllegalArgumentException("storage power must be in [1..6]");
        }

        int lockSize = (int) Math.pow(2, storagePower);
        locks = new NumberedLock[lockSize];
        for (int i = 0; i < locks.length; i++)
            locks[i] = new NumberedLock(i);
    }

    /**
     * Map function between integer and lock from locks array
     *
     * @param id argument
     * @return lock which is result of function
     */
    private NumberedLock getLock(long id) {
        return locks[new Long(id).intValue() & (locks.length - 1)];
    }

    private static final Comparator<? super NumberedLock> CONSISTENT_COMPARATOR = new Comparator<NumberedLock>() {
        @Override
        public int compare(NumberedLock o1, NumberedLock o2) {
            return new Long(o1.id - o2.id).intValue();
        }
    };


    public void lockIds(@NotNull long[] ids) {
        Preconditions.checkNotNull(ids);
        NumberedLock[] neededLocks = getOrderedLocks(ids);
        for (NumberedLock nl : neededLocks)
            nl.lock.lock();
    }

    public void unlockIds(@NotNull long[] ids) {
        Preconditions.checkNotNull(ids);
        NumberedLock[] neededLocks = getOrderedLocks(ids);
        for (NumberedLock nl : neededLocks)
            nl.lock.unlock();
    }

    private NumberedLock[] getOrderedLocks(long[] ids) {
        NumberedLock[] neededLocks = new NumberedLock[ids.length];
        for (int i = 0; i < ids.length; i++) {
            neededLocks[i] = getLock(i);
        }
        Arrays.sort(neededLocks, CONSISTENT_COMPARATOR);
        return neededLocks;
    }
}
 /*   // ...
    public void transfer(StripedLock lock, Account from, Account to) {
        int[] accountIds = new int[]{from.getId(), to.getId()};
        lock.lockIds(accountIds);
        try {
            // profit!
        } finally {
            lock.unlockIds(accountIds);
        }
    }
*/

