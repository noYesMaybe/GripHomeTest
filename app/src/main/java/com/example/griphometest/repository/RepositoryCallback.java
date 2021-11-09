package com.example.griphometest.repository;

public interface RepositoryCallback<T> {

    void onComplete(Result<T> result);
}
