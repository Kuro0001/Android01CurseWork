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
import com.example.cursework.databinding.FragmentHotelBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toast toast;
    public static final String tagDB = "tagFragment";
    private FragmentHotelBinding binding;
    DBHelper dbHelper;
    SQLiteDatabase database;
    SimpleCursorAdapter simpleCursorAdapter;
    Cursor cursor;
    int hotelId;
    int directionId;
    int[] hotelIDs = {0};

    public static String[] names = new String[]{
            DBHelper.KEY_HOTELS_ID,
            "hotelName",
            "hotelADDRESS",
            "hotelDirectionID",
            "DirectionID",
            "DirectionNAME"
    };

    public static String[] from = new String[]{
            names[1],
            names[2],
            names[5]
    };
    public static int[] to = new int[]{
            R.id.etHotelName,
            R.id.etHotelAddress,
            R.id.etHotelDirectionName
    };

    public HotelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelFragment newInstance(String param1, String param2) {
        HotelFragment fragment = new HotelFragment();
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
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        binding = FragmentHotelBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        binding.btnSearch.setOnClickListener(this::onSearch);
        binding.btnAdd.setOnClickListener(this::onAdd);
        binding.lvHotels.setOnItemClickListener(this::onClickList);
        dbHelper = new DBHelper(getContext());
        readDB();
        return root;
//        return inflater.inflate(R.layout.fragment_hotel, container, false);
    }

    @Override
    public void onDestroyView() {
        Log.d(tagDB, "?????????? ???????????? onDestroyView ?????????????????? HotelFragment");
        super.onDestroyView();
        binding = null;
//        cursor.close();
    }

    /**
     * ?????????? ???????????????????? ???????????? ???? ????
     */
    public void readDB() {
        Log.d(tagDB, "?????????? ???????????? readDB ?????????????????? HotelFragment");
        database = dbHelper.getReadableDatabase();

        String query = "SELECT hotel." + DBHelper.KEY_HOTELS_ID +
                ", hotel." + DBHelper.KEY_HOTELS_NAME + " as " + names[1] +
                ", hotel." + DBHelper.KEY_HOTELS_ADDRESS + " as " + names[2] +
                ", direction." + DBHelper.KEY_DIRECTIONS_ID + " as " + names[3] +
                ", direction." + DBHelper.KEY_DIRECTIONS_NAME + " as " + names[5] +
                " FROM " + DBHelper.TABLE_NAME_HOTELS + " as hotel " +
                " INNER JOIN " + DBHelper.TABLE_NAME_DIRECTIONS + " as direction" +
                " on hotel." + DBHelper.KEY_HOTELS_ID_DIRECTION +
                " = direction." + DBHelper.KEY_DIRECTIONS_ID;

        cursor = database.rawQuery(query, null);
        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_hotel, cursor, from, to, 0);
        binding.lvHotels.setAdapter(simpleCursorAdapter);
        binding.lvHotels.setOnItemClickListener(this::onClickList);


        if (cursor.moveToFirst() == true) {
            hotelIDs = new int[cursor.getCount()];
            int i = 0;
            //?????????????????? ????????????????
            do {
                int indID = cursor.getColumnIndex(DBHelper.KEY_HOTELS_ID);
                hotelIDs[i] = cursor.getInt(indID);
                i++;
                simpleCursorAdapter.changeCursor(cursor);
                binding.lvHotels.setOnItemClickListener(this::onClickList);
                cursor.moveToNext();
            } while (cursor.isAfterLast() != true);
        }
//        cursor.close();
    }

    /**
     * ?????????? ?????????????????????????? ???????????????????????? ???? ?????????????? ???????????? (?????????????? ?????????????? ???? ?????????????? ????????????)
     * ?????????????????????????????? ???? ?????????????????? ???????????????????? ????????????
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onClickList(AdapterView<?> parent, View view, int position, long id) {
        cursor.moveToPosition(position);
        int indID = cursor.getColumnIndex(names[0]);
        hotelId = cursor.getInt(indID);
        if (getArguments().getString("action").equals("choose")) {
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            String key = "select_hotel";
            Bundle bundle = new Bundle();
            bundle.putInt(key, hotelId);
            requireActivity().getSupportFragmentManager().setFragmentResult(key, bundle);
            host.popBackStack();
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putInt("id", hotelId);
            NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            host.navigate(R.id.fragment_hotel_detail, bundle);
        }
    }

    /**
     * ?????????? ?????? ???????????????? ?????????? ????????????
     *
     * @param view
     */
    public void onAdd(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", -1);
        NavController host = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        host.navigate(R.id.fragment_hotel_detail, bundle);
    }

    /**
     * ?????????? ?????? ???????????? ???????????? ???? ?????????????????? ??????????????????????
     * @param view
     */
    public void onSearch(View view) {
        String directionName = binding.etSearchDirectionName.getText().toString();
        database = dbHelper.getReadableDatabase();
        String query = "SELECT hotel." + DBHelper.KEY_HOTELS_ID +
                ", hotel." + DBHelper.KEY_HOTELS_NAME + " as " + names[1] +
                ", hotel." + DBHelper.KEY_HOTELS_ADDRESS + " as " + names[2] +
                ", direction." + DBHelper.KEY_DIRECTIONS_ID + " as " + names[3] +
                ", direction." + DBHelper.KEY_DIRECTIONS_NAME + " as " + names[5] +
                " FROM " + DBHelper.TABLE_NAME_HOTELS + " as hotel " +
                " INNER JOIN " + DBHelper.TABLE_NAME_DIRECTIONS + " as direction" +
                " on hotel." + DBHelper.KEY_HOTELS_ID_DIRECTION +
                " = direction." + DBHelper.KEY_DIRECTIONS_ID +
                " WHERE direction." + DBHelper.KEY_DIRECTIONS_NAME +
                " LIKE '%" + directionName + "%'";
        cursor = database.rawQuery(query, null);


        if (cursor.moveToFirst() == true) {
            simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_hotel, cursor, from, to, 0);
            binding.lvHotels.setAdapter(simpleCursorAdapter);
            binding.lvHotels.setOnItemClickListener(this::onClickList);


            if (cursor.moveToFirst() == true) {
                toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_OK, cursor.getCount()), Toast.LENGTH_LONG);
                //?????????????????? ????????????????
                do {
                    simpleCursorAdapter.changeCursor(cursor);
                    binding.lvHotels.setOnItemClickListener(this::onClickList);
                } while (cursor.moveToNext() == true);
            }
        } else {
            toast = Toast.makeText(getContext(), getResources().getString(R.string.action_search_null), Toast.LENGTH_LONG);
            Log.d(tagDB, getResources().getString(R.string.action_search_null));
        }
        toast.show();
    }
}