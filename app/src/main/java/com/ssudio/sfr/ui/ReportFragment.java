package com.ssudio.sfr.ui;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.ReportResponseModel;
import com.ssudio.sfr.report.presenter.IReportView;
import com.ssudio.sfr.report.presenter.ReportPresenter;
import com.ssudio.sfr.utility.DateUtility;

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
 */
public class ReportFragment extends Fragment implements IReportView {
    @Inject
    ReportPresenter presenter;
    @Inject
    LocalAuthenticationService localAuthService;

    @BindView(R.id.txtStartDate)
    protected EditText txtStartDate;

    @BindView(R.id.txtEndDate)
    protected EditText txtEndDate;

    @BindView(R.id.tblLayoutContainer)
    protected TableLayout tblLayoutContainer;

    @BindView(R.id.txtTotal)
    protected TextView txtTotal;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private Date startPeriod;
    private Date endPeriod;

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

        /*ReportComponents components = DaggerReportComponents.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .reportModule(new ReportModule(this))
                .build();*/

        BasePresenterComponent basePresenterComponent = DaggerBasePresenterComponent.builder()
                .sFRApplicationModule(((SFRApplication)getActivity().getApplication()).getSfrApplicationModule())
                .localStorageModule(new LocalStorageModule())
                .localAuthenticationModule(new LocalAuthenticationModule())
                .networkModule(new NetworkModule())
                .build();

        ReportComponents components = basePresenterComponent.newReportSubComponent(new ReportModule(this));

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

        return v;
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String dateStart = df.format(startPeriod);
        String dateEnd = df.format(endPeriod);
        String verificationCode = localAuthService.getLocalVerificationCode();

        ReportRequestModel model = new ReportRequestModel(dateStart, dateEnd, verificationCode);

        presenter.getReport(model);
    }

    @Override
    public void bindReport(final ArrayList<ReportResponseModel> result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bindInfo(result);
            }
        });
    }

    private void bindInfo(ArrayList<ReportResponseModel> result) {
        while (tblLayoutContainer.getChildCount() > 1) {
            tblLayoutContainer.removeView(tblLayoutContainer.getChildAt(tblLayoutContainer.getChildCount() - 1));
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
            lblNo.setText(String.valueOf(i));
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

            total += result.get(i).getAmount();

            tblLayoutContainer.addView(tr);
        }

        bindTotalLabel(total);
    }

    private void bindTotalLabel(double total) {
        txtTotal.setVisibility(View.VISIBLE);
        txtTotal.setText("Jumlah total: " + String.valueOf(total));
    }

    @Override
    public void onDestroy() {
        presenter.unregisterEventHandler();

        super.onDestroy();
    }
}
