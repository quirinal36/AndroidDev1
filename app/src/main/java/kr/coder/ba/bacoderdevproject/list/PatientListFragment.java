package kr.coder.ba.bacoderdevproject.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.coder.ba.bacoderdevproject.MainActivity;
import kr.coder.ba.bacoderdevproject.R;
import kr.coder.ba.bacoderdevproject.model.Patient;
import kr.coder.ba.bacoderdevproject.view.PatientFragment;

public class PatientListFragment extends Fragment {
    private final static String TITLE = "환자리스트";
    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
//    private int mColumnCount = 1;
    public static final String TAG = PatientListFragment.class.getSimpleName();
    RecyclerView recyclerView;
    private PatientAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PatientListFragment newInstance(int columnCount) {
        PatientListFragment fragment = new PatientListFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_patient_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        updateAdapter();

        return recyclerView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "onCreateOptionsMenu");
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }

    private void updateAdapter(){
        final List<Patient> list = new ArrayList<>();
        RequestQueue rq = Volley.newRequestQueue(getContext());
        StringBuilder url = new StringBuilder();
        url.append("http://www.bacoder.kr/getPatients.jsp");
        StringRequest sr = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONObject resultJson = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                            JSONArray array = (JSONArray) resultJson.get("list");
                            Log.d(TAG, "array leng: " + array.length());
                            for(int i=0; i<array.length(); i++){
                                JSONObject obj = new JSONObject(array.getString(i));


                                Patient patient = new Patient();
                                patient.setId(obj.getInt("id"));
                                patient.setName(obj.getString("name"));
                                patient.setPhoto(obj.getString("photo").replaceAll("\\\\", ""));
                                patient.setP_date(obj.getString("p_date"));

                                Log.d(TAG, patient.toString());
                                list.add(patient);
                            }
                            adapter = new PatientAdapter(list);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rq.add(sr);
    }
    @Override
    public void onResume() {
        super.onResume();
        //((MainActivity)getActivity()).getSupportActionBar().setTitle(this.TITLE);
    }

    protected class PatientHolder extends RecyclerView.ViewHolder {
        Patient patient;
        @BindView(R.id.list_title_view)
        TextView titleText;
        @BindView(R.id.list_thumbnail_view)
        ImageView thumbnailImg;
        @BindView(R.id.list_item_view)
        LinearLayout _hoder;

        public PatientHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(Patient patient){
            this.patient = patient;
            titleText.setText(patient.getName());
            _hoder.setTag(patient.getId());
            Picasso.with(getContext()).load(patient.getPhoto()).placeholder(R.drawable.avatar).into(thumbnailImg, new Callback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getContext(), "성공", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public final static String CHOICE = "CHOICE";

    private class PatientAdapter extends RecyclerView.Adapter<PatientHolder> implements View.OnClickListener {
        private List<Patient> items;
        public PatientAdapter(List<Patient> items){
            this.items = items;
        }

        @Override
        public void onClick(View view) {
            Fragment fragment = new PatientFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            Bundle arguments = new Bundle();
            Log.d(TAG, "(int)view.getTag(): " + (int)view.getTag());
            arguments.putInt(CHOICE, (int)view.getTag());
            fragment.setArguments(arguments);

            fragmentTransaction.replace(R.id.content_main , fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @NonNull
        @Override
        public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);
            view.setOnClickListener(this);
            return new PatientHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
            Patient patient = items.get(position);
            holder.bindItem(patient);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
