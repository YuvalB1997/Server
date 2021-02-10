package bgu.spl.net.api;


import bgu.spl.net.impl.BGRSServer.RSMessage;

public interface Process<T> {
    RSMessage func(T data);
}
