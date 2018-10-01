package com.strongest.savingdata.Controllers;

public interface CallBacks {

    public interface OnFinish {
        void onFinish(Object o);
    }

    public interface Observer {

        void notify(Object o);
    }

    public interface Change{
        void onChange();
    }
}
