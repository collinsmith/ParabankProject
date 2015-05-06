package com.parabank.parasoft.app.android;

import android.content.Context;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.parabank.parasoft.app.android.adts.Customer;
import com.parabank.parasoft.app.android.adts.Setting;
import com.parabank.parasoft.app.android.adts.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SettingsAdapter extends ArrayAdapter<Setting> {
    private final User user;
    private final Map<String, ViewHolder> settings;
    private Collection<IBinder> inputs;

    public SettingsAdapter(Context context, User user, List<Setting> settings) {
        super(context, 0, new ArrayList<>(settings));
        this.user = user;
        this.settings = new Hashtable<>();
        inputs = new CopyOnWriteArrayList<>();
    }

    public String getValue(String settingName) {
        return settings.get(settingName).etSettingValue.getText().toString();
    }

    public void hideInputs() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        for (IBinder iBinder : inputs) {
            imm.hideSoftInputFromWindow(iBinder, 0);
        }
    }

    private static class ViewHolder {
        final TextView tvSettingName;
        final EditText etSettingValue;

        ViewHolder(TextView tvSettingName, EditText etSettingValue) {
            this.tvSettingName = tvSettingName;
            this.etSettingValue = etSettingValue;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Setting setting = getItem(position);
        ViewHolder holder = null;
        View view = convertView;

        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.setting_list_item_layout, null);

            TextView tvSettingName = (TextView)view.findViewById(R.id.tvSettingName);
            EditText etSettingValue = (EditText)view.findViewById(R.id.etSettingValue);
            holder = new ViewHolder(tvSettingName, etSettingValue);
            view.setTag(holder);

            etSettingValue.setFocusable(true);
            etSettingValue.setFocusableInTouchMode(true);

            this.settings.put(setting.getId(), holder);
            inputs.add(holder.etSettingValue.getWindowToken());
        }

        Object tag = view.getTag();
        if (tag instanceof ViewHolder) {
            holder = (ViewHolder)tag;
            holder.tvSettingName.setText(setting.getName());
            if (setting.getHint() > 0) {
                holder.etSettingValue.setHint(setting.getHint());
            }

            holder.etSettingValue.setText(setting.getDefaultValue());
            holder.etSettingValue.setInputType(setting.getInputType());
        }

        return view;
    }
}
