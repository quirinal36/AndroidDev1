package kr.coder.ba.bacoderdevproject.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONException;
import org.json.JSONObject;

public class Patient {
    private int id;
    private String photo;
    private String p_date;
    private String name;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getP_date() {
        return p_date;
    }
    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    public static Patient parseTo(JSONObject input) throws JSONException {
        Patient result = new Patient();
        if(input.has("id")){
            result.setId(input.getInt("id"));
        }
        if(input.has("photo")){
            result.setPhoto(input.getString("photo"));
        }
        if(input.has("name")){
            result.setName(input.getString("name"));
        }
        if(input.has("p_date")){
            result.setP_date(input.getString("p_date"));
        }
        return result;
    }

}
