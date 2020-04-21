package ru.crg.reglaments.batchprocessing;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import ru.crg.reglaments.entity.ReglamentLink;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReglamentLinkRowMapper implements RowMapper<ReglamentLink> {

    @Override
    public ReglamentLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReglamentLink reglamentLink = new ReglamentLink();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(rs.getString("reglament"));

            reglamentLink.setUrl(makeUrl(jsonObject.get("url").toString()));
        } catch (JSONException err) {
            return reglamentLink;
        }

        return reglamentLink;
    }

    @Nullable
    private URL makeUrl(String rLink) {
        if (rLink == null) {
            return null;
        }

        try {
            return new URL(rLink);
        } catch (MalformedURLException e) {
            try {
                return new URL("http", "localhost", 8100, rLink);
            } catch (MalformedURLException malformedURLException) {
                return null;
            }
        }
    }

}
