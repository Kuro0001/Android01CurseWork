package com.example.cursework.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.cursework.R;
import com.example.cursework.dataBase.DBHelper;
import com.example.cursework.databinding.FragmentTourOperatorBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourOperatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourOperatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentTourOperatorBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int selectedID;

    public static String[] from = new String[] {
            DBHelper.KEY_TOUR_OPERATORS_NAME,
            DBHelper.KEY_TOUR_OPERATORS_PHONE,
            DBHelper.KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER,
            DBHelper.KEY_TOUR_OPERATORS_MAIL
    };
    public static int[] to = new int[]{
            R.id.etName,
            R.id.etPhone,
            R.id.etUniqueNUmber,
            R.id.etMail
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourOperatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourOperatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourOperatorFragment newInstance(String param1, String param2) {
        TourOperatorFragment fragment = new TourOperatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(tagDB, "?????????? ???????????? onCreateView ?????????????????? TourOperatorFragment");
        // Inflate the layout for this fragment
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentTourOperatorBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.btnSearch.setOnClickListener(this::onSearch);
        binding.lvTourOperators.setOnItemClickListener(this::onClickList);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_tour_operator, container, false);
    }
    @Override
    public void onDestroyView() {
        Log.d(tagDB, "?????????? ???????????? onDestroyView ?????????????????? TourOperatorFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * ?????????? ???????????????????? ???????????? ???? ????
     */
    public void readDB(){
        Log.d(tagDB, "?????????? ???????????? readDB ?????????????????? DirectionFragment");
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_TOUR_OPERATORS,
                null,
                null,
                null,
                null,
                null,
                null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_tour_operator , cursor, from, to, 0);
        binding.lvTourOperators.setAdapter(simpleCursorAdapter);
        binding.lvTourOperators.setOnItemClickListener(this::onClickList);


        if(cursor.moveToFirst() == true){
            //?????????????????? ????????????????
            do {
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvTourOperators.setOnItemClickListener(this::onClickList);
            } while (cursor.moveToNext() == true);
        }
//        cursor.close();
    }

    /**
     * ?????????? ?????????????????????????? ???????????????????????? ???? ?????????????? ???????????? (?????????????? ?????????????? ???? ?????????????? ????????????)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onClickList(AdapterView<?> parent, View view, int position, long id){
        if (getArguments().getString("action").equals("choose")) {
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            String key = "select_tour_operator";
            Bundle bundle = new Bundle();
            bundle.putInt(key, (int)id);
            requireActivity().getSupportFragmentManager().setFragmentResult(key, bundle);
            host.popBackStack();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putInt("id", (int) id);
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            host.navigate(R.id.fragment_tour_operator_detail, bundle);
        }
    }
    /**
     * ?????????? ?????? ???????????????? ?????????? ????????????
     * @param view
     */
    public void onAdd(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_tour_operator_detail,bundle);
    }

    /**
     * ?????????? ?????? ???????????? ?????????????????????????? ???? ?????????????????? ???????????????????????? ?? ?????????????????????? ????????????
     * @param view
     */
    public void onSearch(View view){
        String enteredName = binding.etName.getText().toString();
        String enteredNumber = binding.etNuqueNumber.getText().toString();
        database = dbHelper.getReadableDatabase();
        cursor = database.query(DBHelper.TABLE_NAME_TOUR_OPERATORS,
                null,
                DBHelper.KEY_TOUR_OPERATORS_NAME + " LIKE ? AND " + DBHelper.KEY_TOUR_OPERATORS_UNIQUE_COMPANY_NUMBER + " LIKE ?",
                new String[] {
                        enteredName + "%", enteredNumber + "%"},
                null,
                null,
                null);
        if(cursor.moveToFirst() == true) {
            int indId = cursor.getColumnIndex(DBHelper.KEY_TOUR_OPERATORS_ID);
            selectedID = cursor.getInt(indId);
            {
                simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_tour_operator , cursor, from, to, 0);
                binding.lvTourOperators.setAdapter(simpleCursorAdapter);
                binding.lvTourOperators.setOnItemClickListener(this::onClickList);
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_OK,cursor.getCount()),Toast.LENGTH_LONG);
                Log.d(tagDB, getResources().getString(R.string.action_search_OK,cursor.getCount()));
                if(cursor.moveToFirst() == true){
                    //?????????????????? ????????????????
                    do {
                        simpleCursorAdapter.changeCursor(cursor);
                        binding.lvTourOperators.setOnItemClickListener(this::onClickList);
                    } while (cursor.moveToNext() == true);
                }
            }
        }else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_null),Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_search_null));
        }
        toast.show();
    }
}