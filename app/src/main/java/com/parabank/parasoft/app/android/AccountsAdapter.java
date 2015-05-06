package com.parabank.parasoft.app.android;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parabank.parasoft.app.android.adts.Account;
import com.parabank.parasoft.app.android.adts.User;

import java.util.List;

public class AccountsAdapter extends ArrayAdapter<Account> implements View.OnClickListener {
    private final User user;

    public AccountsAdapter(Context context, User user, List<Account> accounts) {
        super(context, 0, accounts);
        this.user = user;
    }

    private static class ViewHolder {
        final TextView tvAccountInfo;
        final ImageButton btnRequestLoan;

        ViewHolder(TextView tvAccountInfo, ImageButton btnRequestLoan) {
            this.tvAccountInfo = tvAccountInfo;
            this.btnRequestLoan = btnRequestLoan;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        ViewHolder holder = null;
        View view = convertView;

        //if (view == null) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.account_list_item_layout, null);

        TextView tvAccountInfo = (TextView)view.findViewById(R.id.tvAccountInfo);
        ImageButton btnRequestLoan = (ImageButton)view.findViewById(R.id.btnRequestLoan);
        view.setTag(new ViewHolder(tvAccountInfo, btnRequestLoan));

        //tvItemText.setSelected(true);
        //}

        Object tag = view.getTag();
        if (tag instanceof ViewHolder) {
            holder = (ViewHolder)tag;
        }

        if (account != null && holder != null) {
            if (holder.tvAccountInfo != null) {
                SpannableStringBuilder builder = new SpannableStringBuilder();

                String temp;
                SpannableString text;
                temp = Long.toString(account.getId());
                text = new SpannableString(temp);
                text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.parasoft_blue)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(text);

                temp = " [ ";
                text = new SpannableString(temp);
                text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.black)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(text);

                temp = account.getType();
                text = new SpannableString(temp);
                text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.parasoft_blue)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(text);

                temp = " ] ";
                text = new SpannableString(temp);
                text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.black)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(text);

                temp = account.getBalance();
                double balance = Double.parseDouble(account.getBalance());
                text = new SpannableString(temp);
                text.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(balance < 0 ? android.R.color.holo_red_light : android.R.color.holo_green_light)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(text);

                holder.tvAccountInfo.setText(builder);

                btnRequestLoan.setOnClickListener(this);
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder)v.getTag();

        AlertDialog requestLoanDialog = new AlertDialog.Builder(getContext())
                .setTitle("Request Loan")
                .create();

        requestLoanDialog.show();
    }
}
