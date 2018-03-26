package com.server;

    public class DuplicateUsernameException extends Exception {
        public DuplicateUsernameException(String message){
            super(message);
        }
    }
