package dawson.dawsondangerousclub;

import android.os.Bundle;

public class WhereIsFriendActivity extends OptionsMenu {

    private static final String WHERE_IS_FRIENDS_URL = "https://dawsondangerousclub2.herokuapp.com/api/api/whereisfriend?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_where_is_friend);


    }
}
