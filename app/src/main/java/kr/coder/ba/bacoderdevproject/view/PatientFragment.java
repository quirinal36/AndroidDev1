package kr.coder.ba.bacoderdevproject.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import kr.coder.ba.bacoderdevproject.MainActivity;
import kr.coder.ba.bacoderdevproject.R;
import kr.coder.ba.bacoderdevproject.list.PatientListFragment;
import kr.coder.ba.bacoderdevproject.model.Patient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    public final String TITLE = "환자정보";
    // TODO: Rename and change types of parameters
    private int patientId;
//    private String mParam2;

    public String getTITLE() {
        return TITLE;
    }

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        args.putString(PatientListFragment.CHOICE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt(PatientListFragment.CHOICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity)getActivity()).getSupportActionBar().setTitle(this.TITLE);
        getPatientInfo(patientId);
    }
    private void getPatientInfo(final int id){
        final String url = getString(R.string.server_address) + "/getPatient.jsp";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject patientJson = new JSONObject(URLDecoder.decode(response, "UTF-8"));
                    Patient patient = Patient.parseTo(patientJson);

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(patient.getName() +"님의 정보");
                }catch(JSONException e){
                    e.printStackTrace();
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                return map;
            }
        };

        Volley.newRequestQueue(getContext()).add(sr);
    }

}
