package Helper;

/**
 * Created by jason on 10/03/2017.
 */
public interface ResponseCallback<T extends Object> {

    void onSuccess();

    void onError();
}
