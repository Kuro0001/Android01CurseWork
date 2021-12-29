package com.example.cursework.ui.account;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cursework.Authorisation;
import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentAccountBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    int selectedId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = new DBHelper(getContext());
        binding.btnLogIn.setOnClickListener(this::onLogIn);
        binding.btnLogOut.setOnClickListener(this::onLogOut);
        binding.btnGotoAccount.setOnClickListener(this::onGoToAccount);
        setButtonsState();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Метод по предохранению ползователя от нажатия нежелательных кнопок в зависимости от состояния авторизации
     */
    public void setButtonsState(){
        if (Authorisation.isLoggedIn){
            binding.btnLogIn.setEnabled(false);
            binding.btnLogOut.setEnabled(true);
            binding.btnGotoAccount.setEnabled(true);
        }
        else {
            binding.btnLogIn.setEnabled(true);
            binding.btnLogOut.setEnabled(false);
            binding.btnGotoAccount.setEnabled(false);
        }
    }

    /**
     * Проверка наличия выбранной записи данных в БД
     * @param table
     * @param columnName
     * @param columnContent
     * @return
     */
    public boolean rowIsExist(String table, String columnName, String columnContent){
        database = dbHelper.getReadableDatabase();
        cursor = database.query(table,
                null,
                columnName + " = ?",
                new String[] {
                        columnContent},
                null,
                null,
                null);
        if(cursor.moveToFirst() == true)
            return true;
        return false;
    }
    public int findUser(String login, String password){
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_EMPLOYEES,
                null,
                DBHelper.KEY_EMPLOYEES_LOGIN + " = ? AND " + DBHelper.KEY_EMPLOYEES_PASSWORD + " = ?",
                new String[] {
                        login, password},
                null,
                null,
                null);
        if(cursor.moveToFirst() == true){
            int indID = cursor.getColumnIndex(DBHelper.KEY_EMPLOYEES_ID);
            return cursor.getInt(indID);
        }
        return -1;
    }

    /**
     * Метод авторизации
     * @param view
     */
    public void onLogIn(View view){
        Log.d(tagDB, "Вызов метода onLogIn фрагмента AccountFragment");
        database = dbHelper.getReadableDatabase();
        String login = binding.etLogin.getText().toString();
        String password = binding.etPassword.getText().toString();
        if (rowIsExist(DBHelper.TABLE_NAME_EMPLOYEES, DBHelper.KEY_EMPLOYEES_LOGIN, login)) {
            int id = findUser(login, password);
            if (id >= 0){
                Authorisation.isLoggedIn = true;
                Authorisation.id = id;
                setButtonsState();
                Log.d(tagDB, getResources().getString(R.string.action_result_OK) + getResources().getString(R.string.action_authorisation_log_in_OK));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_authorisation_log_in_OK), Toast.LENGTH_LONG);
            }else {
                Log.d(tagDB, getResources().getString(R.string.action_authorisation_wrong_password));
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_authorisation_wrong_password),Toast.LENGTH_LONG);
            }
        }else {
            Log.d(tagDB, getResources().getString(R.string.action_authorisation_login_not_exist));
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_authorisation_login_not_exist), Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * Метод отмены авторизации
     * @param view
     */
    public void onLogOut(View view){
        Log.d(tagDB, "Вызов метода onLogOut фрагмента AccountFragment");
        Authorisation.isLoggedIn = false;
        Authorisation.id = -1;
        setButtonsState();
        Log.d(tagDB, getResources().getString(R.string.action_result_OK) + getResources().getString(R.string.action_authorisation_log_out_OK));
        toast = Toast.makeText(getContext(), getResources().getString(R.string.action_authorisation_log_out_OK), Toast.LENGTH_LONG);
        toast.show();
    }

    public void onGoToAccount(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", Authorisation.id);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_employee_detail,bundle);
    }
}