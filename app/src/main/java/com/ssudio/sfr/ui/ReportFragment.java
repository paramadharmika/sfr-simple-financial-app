package com.ssudio.sfr.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.ssudio.sfr.R;
import com.ssudio.sfr.SFRApplication;

import com.ssudio.sfr.authentication.LocalAuthenticationService;
import com.ssudio.sfr.components.ui.BasePresenterComponent;
import com.ssudio.sfr.components.ui.DaggerBasePresenterComponent;
import com.ssudio.sfr.components.ui.ReportComponents;
import com.ssudio.sfr.modules.LocalAuthenticationModule;
import com.ssudio.sfr.modules.LocalStorageModule;
import com.ssudio.sfr.modules.NetworkModule;
import com.ssudio.sfr.modules.ReportModule;
import com.ssudio.sfr.network.event.NetworkConnectivityEvent;
import com.ssudio.sfr.network.ui.IConnectivityListenerView;
import com.ssudio.sfr.network.ui.ILoadingView;
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.ReportResponseModel;
import com.ssudio.sfr.report.model.UploadReportModel;
import com.ssudio.sfr.report.presenter.IReportView;
import com.ssudio.sfr.report.presenter.ReportPresenter;
import com.ssudio.sfr.utility.DateUtility;
import com.ssudio.sfr.utility.ImageSelectorUtility;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * TODO: create custom validator
 *  http://www.bernie-eng.com/blog/2015/03/25/validation-with-android-saripaar/
 */
public class ReportFragment extends Fragment
        implements IReportView, IConnectivityListenerView, ILoadingView, Validator.ValidationListener {

    private static final int SELECT_FILE = 567483;

    @Inject
    LocalAuthenticationService localAuthService;
    @Inject
    ReportPresenter reportPresenter;


    @Required(order = 1)
    @BindView(R.id.txtStartDate)
    protected EditText txtStartDate;

    @Required(order = 2)
    @BindView(R.id.txtEndDate)
    protected EditText txtEndDate;

    @BindView(R.id.tblLayoutContainer)
    protected TableLayout tblLayoutContainer;

    @BindView(R.id.txtTotal)
    protected TextView txtTotal;

    private Button selectedUploadButton;

    private Validator userDetailsValidator;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private Date startPeriod;
    private Date endPeriod;
    private int selectedId;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        ButterKnife.bind(this, v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        ReportComponents components = basePresenterComponent.newReportSubComponent(new ReportModule(this, this, this));

        components.inject(this);

        txtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDateDialogForStartPeriode();
                }
            }
        });
        txtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDateDialogForEndPeriode();
                }
            }
        });

        disableSofInputOnFocus(txtStartDate);
        disableSofInputOnFocus(txtEndDate);

        setupValidators();

        return v;
    }

    private void disableSofInputOnFocus(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod("setShowSoftInputOnFocus",
                        new Class[]{boolean.class});

                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private void setupValidators() {
        userDetailsValidator = new Validator(this);

        //'this' class implements ValidationListener
        userDetailsValidator.setValidationListener(this);
    }

    protected void showDateDialogForStartPeriode() {
        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        startPeriod = newDate.getTime();

                        txtStartDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();
    }

    protected void showDateDialogForEndPeriode() {
        Calendar newCalendar = Calendar.getInstance();

        toDatePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        endPeriod = newDate.getTime();

                        txtEndDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog.show();
    }

    @OnClick(R.id.btnSubmit)
    protected void btnSubmit_Click() {
        userDetailsValidator.validate();
    }

    @Override
    public IContainerViewCallback getParentView() {
        return (IContainerViewCallback)getActivity();
    }

    @Override
    public void bindReport(final ArrayList<ReportResponseModel> result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result.size() == 0) {
                    getParentView().showMessage(false,
                            getResources().getString(R.string.message_report_not_found));
                } else {
                    bindInfo(result);
                }
            }
        });
    }

    private void bindInfo(ArrayList<ReportResponseModel> result) {
        while (tblLayoutContainer.getChildCount() > 1) {
            View v = tblLayoutContainer.getChildAt(tblLayoutContainer.getChildCount() - 1);

            tblLayoutContainer.removeView(v);
        }

        double total = 0;

        int id = 0;
        for (int i = 0; i < result.size(); i++) {
            TableRow tr = new TableRow(getActivity());

            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(10, 4, 10, 0);
            tr.setLayoutParams(trParams);


            if (i % 2 != 0) {
                tr.setBackgroundColor(Color.GRAY);
            }

            tr.setId(id++);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView lblNo = new TextView(getActivity());
            lblNo.setId(id++);
            lblNo.setText(String.valueOf(i + 1));
            lblNo.setPadding(2, 0, 5, 0);
            /*lblNo.setTextColor(Color.WHITE);*/
            tr.addView(lblNo);

            TextView lblDate = new TextView(getActivity());
            lblDate.setId(id++);
            lblDate.setText(DateUtility.parseToApplicationDateFormat(result.get(i).getDate()));
            /*lblDate.setTextColor(Color.WHITE);*/
            tr.addView(lblDate);

            TextView lblName = new TextView(getActivity());
            lblName.setId(id++);
            lblName.setText(result.get(i).getName());
            lblName.setPadding(2, 0, 5, 0);
            /*lblName.setTextColor(Color.WHITE);*/
            tr.addView(lblName);

            TextView lblAmount = new TextView(getActivity());
            lblAmount.setId(id++);
            lblAmount.setText(String.valueOf(result.get(i).getAmount()));
            /*lblAmount.setTextColor(Color.WHITE);*/
            tr.addView(lblAmount);

            final int temporaryId = id++;
            Button btnUpload = new Button(getActivity());
            btnUpload.setId(temporaryId);
            btnUpload.setTag(result.get(i).getId());
            btnUpload.setText("Upload");

            int proofId;
            try {
                proofId = Integer.parseInt(result.get(i).getProofId());
            } catch (Exception e) {
                proofId = 0;
            }

            if (proofId >= 1) {
                btnUpload.setVisibility(View.GONE);
            } else {
                btnUpload.setVisibility(View.VISIBLE);

                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int reportId = Integer.valueOf(v.getTag().toString());

                        showUploadCamera(reportId, temporaryId);
                    }
                });
            }

            //todo: hide the upload button
            btnUpload.setVisibility(View.GONE);
            tr.addView(btnUpload);

            total += result.get(i).getAmount();

            tblLayoutContainer.addView(tr);
        }

        bindTotalLabel(total);
    }

    private void showUploadCamera(int reportId, int temporaryId) {
        if (ImageSelectorUtility.checkPermission(getActivity())) {
            this.selectedId = reportId;

            selectedUploadButton = (Button) getActivity().findViewById(temporaryId);

            showGallery();
        }
    }

    private void showGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void bindTotalLabel(double total) {
        txtTotal.setVisibility(View.VISIBLE);
        txtTotal.setText(getResources().getString(R.string.total_amount) + String.valueOf(total));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onImageSelected(data);
            }
        }
    }

    private void onImageSelected(Intent data) {
        //Bitmap bm = null;

        if (data != null) {
            //bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());

            Uri videoUri = data.getData();

            String selectedPath = getPath(videoUri);

            UploadReportModel model = new UploadReportModel(
                    localAuthService.getLocalVerificationCode(), selectedPath, selectedId);

            executeMultipartPost(model);
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

        assert cursor != null;

        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
    }

    public void executeMultipartPost(UploadReportModel model) {
        reportPresenter.uploadFile(model);
    }

    @Override
    public void showMessage(final NetworkConnectivityEvent e) {
        final Activity activity = getActivity();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;

                if (e.isConnected()) {
                    message = getString(R.string.message_network_connected);
                } else {
                    message = getString(R.string.message_network_is_not_connected);
                }

                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showLoading() {
        getParentView().showLoading();
    }

    @Override
    public void dismissLoading() {
        getParentView().dismissLoading();
    }

    @Override
    public void onValidationSucceeded() {
        clearErrorMessageOnEditText();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String dateStart = df.format(startPeriod);
        String dateEnd = df.format(endPeriod);
        String verificationCode = localAuthService.getLocalVerificationCode();

        ReportRequestModel model = new ReportRequestModel(dateStart, dateEnd, verificationCode);

        reportPresenter.getReport(model);
    }

    private void clearErrorMessageOnEditText() {
        txtEndDate.setError(null);
        txtStartDate.setError(null);
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String failureMessage = failedRule.getFailureMessage();

        failedView.requestFocus();

        if (failedView instanceof EditText) {
            ((EditText)failedView).setError(failureMessage);
        }
    }

    public void showReportItemUploaded(boolean isSuccess, String message) {
        if (isSuccess) {
            selectedId = 0;
            selectedUploadButton.setVisibility(View.GONE);
            selectedUploadButton = null;
        }

        getParentView().showMessage(isSuccess, message);
    }

    @Override
    public void onDestroy() {
        reportPresenter.unregisterEventHandler();

        super.onDestroy();
    }
}
