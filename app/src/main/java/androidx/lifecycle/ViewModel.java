package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 viewModel是一个类，负责为 activity 活动或 fragment 准备和管理数据。
 它还处理 activity / fragment 与 application  其余部分的通信（例如，调用业务逻辑类）。
 <P>
 ViewModel 总是与作用域（activity 或 fragment）关联创建的，并且只要作用域是活动的，它就将被保留。例如，如果它是一个 activity，直到完成为止。
 <P>
 换句话说，这意味着，如果由于配置更改（例如旋转）而销毁了 ViewModel 的所有者，则该 ViewModel 将不会被销毁。所有者的新实例将重新连接到现有的 ViewModel。
 <P>
 ViewModel 的目的是获取和保存 Activity 或 Fragment 所需的信息。Activity 或 Fragment 应该能够观察到 ViewModel 中的更改。
 ViewModels 通常通过 LiveData 或 Android 数据绑定公开此信息。您还可以使用您喜欢的框架中的任何可观察性构造。
 <P>
 ViewModel 的唯一职责是管理UI的数据。它不应访问视图层次结构或保留对 Activity 或 Fragment 的引用。
 <P>
 从 Activity 的角度来看，典型的用法是：
 < PRE>

 * public class UserActivity extends Activity {
 *
 *     {@literal @}Override
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.user_activity_layout);
 *
 *         final UserModel viewModel = ViewModelProviders.of(this).get(UserModel.class);
 *
 *         viewModel.getUser().observe(this, new Observer<User>() {
 *            {@literal @}Override
 *             public void onChanged(@Nullable User data) {
 *                 // update ui.
 *             }
 *         });
 *
 *         findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
 *             {@literal @}Override
 *             public void onClick(View v) {
 *                  viewModel.doAction();
 *             }
 *         });
 *     }
 * }
 * </pre>
 *
 * ViewModel 将是:
 * <pre>
 * public class UserModel extends ViewModel {
 *     private final MutableLiveData<User> userLiveData = new MutableLiveData();
 *
 *     public LiveData<User> getUser() {
 *         return userLiveData;
 *     }
 *
 *     public UserModel() {
 *         // trigger user load. 触发用户加载
 *     }
 *
 *     void doAction() {
 *         // depending on the action, do necessary business logic calls and update the userLiveData.
 *         // 根据操作，进行必要的业务逻辑调用并更新 userLiveData。
 *     }
 * }
 * </pre>
 *
 * <p>
 *  ViewModels 还可以用作 Activity 及不同 Fragment 之间的通信层。
 *  每个 Fragment 可以通过其 Activity 使用相同的 key 来获取 ViewModel。
 *  这允许 Fragment 之间以一种非耦合的方式进行通信，这样它们就不需要直接与另一个 Fragment 通信。
 * <pre>
 * public class MyFragment extends Fragment {
 *     public void onStart() {
 *         UserModel userModel = ViewModelProviders.of(getActivity()).get(UserModel.class);
 *     }
 * }
 * </pre>
 * </>
 */
public abstract class ViewModel {
    @Nullable
    private final ConcurrentHashMap<String, Object> mBagOfTags = new ConcurrentHashMap<>();
    private volatile boolean mCleared = false;

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     * 当不再使用此 ViewModel 时，将调用此方法并将其销毁。
     * <p>
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     * 当 ViewModel 观察到一些数据，并且您需要清除此订阅以防止此 ViewModel 泄漏时，它非常有用。
     */
    @SuppressWarnings("WeakerAccess")
    protected void onCleared() {
    }

    @MainThread
    final void clear() {
        mCleared = true;
        // Since clear() is final, this method is still called on mock objects
        // and in those cases, mBagOfTags is null. It'll always be empty though
        // because setTagIfAbsent and getTag are not final so we can skip
        // clearing it
        // 由于clear（）是final，所以仍然对模拟对象调用此方法，在这些情况下，mBagOfTags 为空。
        // 但它总是空的，因为 setTagIfAbsent 和 getTag 不是最终版本，所以我们可以跳过清除它。
        if (mBagOfTags != null) {
            for (Object value : mBagOfTags.values()) {
                // see comment for the similar call in setTagIfAbsent 请参阅 setTagIfAbsent 中类似调用的注释
                closeWithRuntimeException(value);
            }
        }
        onCleared();
    }

    /**
     * 设置与此ViewModel关联的标记和键。
     * 如果给定的 newValue 是 Closeable，它将关闭一次 clear（）。
     * < P>
     * 如果已经为给定的键设置了值，则此调用不执行任何操作，并且返回当前关联的值，给定的 newValue 将被忽略
     * < P>
     * 如果 viewModel 已经清除，那么如果返回的对象实现了 Closeable，则会对该对象调用 close（）。同一对象可能会收到多个关闭调用，因此方法应该是等幂的。
     */
    <T> T setTagIfAbsent(String key, T newValue) {
        assert mBagOfTags != null;
        @SuppressWarnings("unchecked")
        T previous = (T) mBagOfTags.putIfAbsent(key, newValue);
        T result = previous == null ? newValue : previous;
        if (mCleared) {
            // It is possible that we'll call close() multiple times on the same object, but
            // Closeable interface requires close method to be idempotent:
            // "if the stream is already closed then invoking this method has no effect." (c)
            // 我们可能会在同一个对象上多次调用close（），但 Closeable 接口要求 close 方法为等幂：“如果流已经关闭，则调用此方法无效。”
            closeWithRuntimeException(result);
        }
        return result;
    }

    /**
     * Returns the tag associated with this viewmodel and the specified key.
     * 返回与此ViewModel和指定键关联的标记。
     */
    @SuppressWarnings("TypeParameterUnusedInFormals")
    <T> T getTag(String key) {
        //noinspection unchecked
        return (T) mBagOfTags.get(key);
    }

    // 我们可能会在同一个对象上多次调用close（），但 Closeable 接口要求 close 方法为等幂：“如果流已经关闭，则调用此方法无效。”
    private static void closeWithRuntimeException(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
