package com.bracketcove.postrainer.base;


import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This Class contains the Generalized things (Types it can recieve, and Actions it can take)
 * that any Use Case must do in this App.
 *
 * If you are wondering what's going on in the <> beside UseCase below, check out "Java Generic
 * Types".
 *
 * What we're trying to achieve, is to have our subclasses of UseCase be able
 * to work with whatever Types they need, which can and probably will be different.
 *
 * Generic Types are a more Advanced Topic imho, so don't feel bad if they are a bit confusing at
 * first. For a simpler and practical example, try working with an AsyncTask. Think about how you
 * can give it different kinds of Data, but the functionality of it is still super similar.
 */

public abstract class UseCase<RequestModel extends UseCase.RequestValues,
        ResponseModel extends UseCase.ResponseValue> {

    //We use this to manage and appropriately dispose of Observables
    private final CompositeDisposable compositeDisposable;

    //This is what gets sent in to a given UseCase.
    private RequestModel requestModel;

    //This is how we "call back" to the Presenter, which can be thought of as the Subscriber in Rx.
    private Observable<ResponseModel> responseModelObservable;

    public UseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public Observable<ResponseModel> getResponse() {
        return responseModelObservable;
    }

    public void setUseCaseCallback(Observable<ResponseModel> responseModelObservable) {
        this.responseModelObservable = responseModelObservable;
    }

    void run() {
        executeUseCase(requestModel);
    }

    protected abstract void executeUseCase(RequestModel requestModel);

    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    public interface ResponseValue {
    }

    public interface UseCaseCallback<ResponseModel> {
        void onSuccess(ResponseModel response);

        void onError();
    }
}