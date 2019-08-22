package com.u9porn.ui.about;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIPackageHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.sdsmdg.tastytoast.TastyToast;
import com.u9porn.R;
import com.u9porn.data.model.UpdateVersion;
import com.u9porn.service.UpdateDownloadService;
import com.u9porn.ui.MvpActivity;
import com.u9porn.utils.ApkVersionUtils;
import com.u9porn.utils.AppCacheUtils;
import com.u9porn.utils.DialogUtils;
import com.u9porn.utils.GlideApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.common.io.FileUtils;
import ru.noties.markwon.Markwon;

/**
 * @author flymegoc
 */
public class AboutActivity extends MvpActivity<AboutView, AboutPresenter> implements AboutView {

    private static final String TAG = AboutActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.version)
    TextView mVersionTextView;
    @BindView(R.id.about_list)
    QMUIGroupListView mAboutGroupListView;
    @BindView(R.id.copyright)
    TextView mCopyrightTextView;

    private AlertDialog alertDialog;
    private AlertDialog cleanCacheDialog;

    private QMUICommonListItemView cleanCacheQMUICommonListItemView;

    @Inject
    protected AboutPresenter aboutPresenter;
    private TextView commonQuestionTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initToolBar(toolbar);

        initAboutSection();

        mVersionTextView.setText("v" + QMUIPackageHelper.getAppVersion(this));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new java.util.Date());
        mCopyrightTextView.setText(String.format(getResources().getString(R.string.about_copyright), currentYear));

        alertDialog = DialogUtils.initLoadingDialog(this, "正在检查更新，请稍后...");
        presenter.countCacheFileSize(getString(R.string.about_item_clean_cache));
    }

    private void initAboutSection() {
        mAboutGroupListView.setSeparatorStyle(QMUIGroupListView.SEPARATOR_STYLE_NORMAL);
        cleanCacheQMUICommonListItemView = mAboutGroupListView.createItemView(getString(R.string.about_item_clean_cache));
        cleanCacheQMUICommonListItemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        QMUILoadingView loadingView = new QMUILoadingView(this);
        cleanCacheQMUICommonListItemView.addAccessoryCustomView(loadingView);

        QMUIGroupListView.newSection(this)

                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_github)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://github.com/techGay/v9porn";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_item_homepage)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://github.com/techGay/v9porn/issues";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .addItemView(mAboutGroupListView.createItemView("赞赏我，请我喝杯咖啡"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAppreciateDialog();
                    }
                })
                .addItemView(cleanCacheQMUICommonListItemView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChoiceCacheCleanDialog();
                    }
                })
                .addItemView(mAboutGroupListView.createItemView("常见问题"), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCommonQuestionsDialog();
                    }
                })
                .addItemView(mAboutGroupListView.createItemView(getResources().getString(R.string.about_check_update)), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int versionCode = ApkVersionUtils.getVersionCode(AboutActivity.this);
                        if (versionCode == 0) {
                            showMessage("获取应用本版失败", TastyToast.ERROR);
                            return;
                        }
                        alertDialog.show();
                        presenter.checkUpdate(versionCode);
                    }
                })
                .addTo(mAboutGroupListView);

    }

    private void showCommonQuestionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("常见问题");
        View view = View.inflate(this, R.layout.layout_common_questions, null);
        commonQuestionTextView = view.findViewById(R.id.tv_common_question);
        Markwon.setMarkdown(commonQuestionTextView, "**加载中...**");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commonQuestionTextView=null;
                dialog.dismiss();
            }
        });
        builder.show();
        presenter.commonQuestions();
    }

    private void showAppreciateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("赞赏作者");
        View view = View.inflate(this, R.layout.layout_appreciate_qrcode, null);
        final ImageView imageViewWebChat = view.findViewById(R.id.iv_appreciate_qr_code_web_chat);
        final ImageView imageViewAliPay = view.findViewById(R.id.iv_appreciate_qr_code_ali_pay);
        final RadioGroup radioGroup = view.findViewById(R.id.rg_pay);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_web_chat:
                        imageViewWebChat.setVisibility(View.VISIBLE);
                        imageViewAliPay.setVisibility(View.GONE);
                        break;
                    case R.id.rb_ali_pay:
                        imageViewWebChat.setVisibility(View.GONE);
                        imageViewAliPay.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        builder.setView(view);
        builder.setNegativeButton("算了，囊中羞涩", null);
        builder.setPositiveButton("保存至相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rb_ali_pay) {
                    saveToSystemGallery("zhi_fu_bao", R.drawable.alipay1547141972480);
                } else {
                    saveToSystemGallery("wei_xin", R.drawable.mm_reward_qrcode_1547141812376);
                }
            }
        });
        builder.show();
    }

    private void saveToSystemGallery(final String name, int id) {
        GlideApp.with(this).downloadOnly().load(id).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name + ".jpg");
                try {
                    FileUtils.copyFile(resource, file);
                    showMessage("保存图片成功了", TastyToast.SUCCESS);
                    notifySystemGallery(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    showMessage("保存图片失败了", TastyToast.ERROR);
                }
            }
        });
    }

    private void notifySystemGallery(File file) {

        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, new String[]{"image/jpeg"}, null);
    }

    private void showChoiceCacheCleanDialog() {
        final String[] items = new String[]{
                "网页缓存(" + AppCacheUtils.getRxcacheFileSizeStr(this) + ")",
                "视频缓存(" + AppCacheUtils.getVideoCacheFileSizeStr(this) + ")",
                "图片缓存(" + AppCacheUtils.getGlidecacheFileSizeStr(this) + ")"
        };
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(this)
                .setCheckedItems(new int[]{1})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setTitle("请选择要清除的缓存");
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("清除", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                actionCleanFile(builder);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void actionCleanFile(QMUIDialog.MultiCheckableDialogBuilder builder) {
        int selectIndexLength = builder.getCheckedItemIndexes().length;
        List<File> fileDirList = new ArrayList<>();
        for (int i = 0; i < selectIndexLength; i++) {
            int indexCheck = builder.getCheckedItemIndexes()[i];
            switch (indexCheck) {
                case 0:
                    fileDirList.add(AppCacheUtils.getRxCacheDir(AboutActivity.this));
                    break;
                case 1:
                    fileDirList.add(AppCacheUtils.getVideoCacheDir(AboutActivity.this));
                    break;
                case 2:
                    fileDirList.add(AppCacheUtils.getGlideDiskCacheDir(AboutActivity.this));
                default:
            }
        }
        if (fileDirList.size() == 0) {
            showMessage("未选择任何条目，无法清除缓存", TastyToast.INFO);
            return;
        }
        presenter.cleanCacheFile(fileDirList);
    }

    private String getCleanCacheTitle() {
        String zeroFileSize = "0 B";
        String fileSizeStr = AppCacheUtils.getAllCacheFileSizeStr(this);
        if (zeroFileSize.equals(fileSizeStr)) {
            return getResources().getString(R.string.about_item_clean_cache);
        }
        return getResources().getString(R.string.about_item_clean_cache) + "(" + fileSizeStr + ")";
    }

    @NonNull
    @Override
    public AboutPresenter createPresenter() {
        return aboutPresenter;
    }

    private void showUpdateDialog(final UpdateVersion updateVersion) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("发现新版本--v" + updateVersion.getVersionName())
                .setMessage(updateVersion.getUpdateMessage())
                .addAction("立即更新", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        showMessage("开始下载", TastyToast.INFO);
                        Intent intent = new Intent(AboutActivity.this, UpdateDownloadService.class);
                        intent.putExtra("updateVersion", updateVersion);
                        startService(intent);
                    }
                })
                .addAction("稍后更新", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void needUpdate(UpdateVersion updateVersion) {
        showUpdateDialog(updateVersion);
    }

    @Override
    public void noNeedUpdate() {
        showMessage("当前已是最新版本", TastyToast.SUCCESS);
    }

    @Override
    public void checkUpdateError(String message) {
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        alertDialog.show();
    }

    @Override
    public void showContent() {
        dismissDialog();
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void showCleanDialog(String message) {
        cleanCacheDialog = DialogUtils.initLoadingDialog(this, message);
        cleanCacheDialog.show();
    }

    @Override
    public void cleanCacheSuccess(String message) {
        dismissDialog();
        cleanCacheQMUICommonListItemView.setText(getCleanCacheTitle());
        showMessage(message, TastyToast.SUCCESS);
    }

    @Override
    public void cleanCacheFailure(String message) {
        dismissDialog();
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void finishCountCacheFileSize(String message) {
        cleanCacheQMUICommonListItemView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        cleanCacheQMUICommonListItemView.setText(message);
    }

    @Override
    public void countCacheFileSizeError(String message) {
        showMessage(message, TastyToast.ERROR);
    }

    @Override
    public void loadCommonQuestionsSuccess(String mdString) {
        Logger.t(TAG).d(mdString);
        if (commonQuestionTextView != null) {
            Markwon.setMarkdown(commonQuestionTextView, mdString);
        }
    }

    @Override
    public void loadCommonQuestionsFailure(String errorMessage, int code) {
        showError(errorMessage);
    }

    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing() && !isFinishing()) {
            alertDialog.dismiss();
        } else if (cleanCacheDialog != null && cleanCacheDialog.isShowing() && !isFinishing()) {
            cleanCacheDialog.dismiss();
        }
    }
}
