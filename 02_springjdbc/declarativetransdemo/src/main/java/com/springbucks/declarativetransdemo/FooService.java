package com.springbucks.declarativetransdemo;

public interface FooService {
    public void insertRecord();
    public void insertThenRollback() throws RollbackException;
    public void invokeInsertThenRollback() throws RollbackException;
}
