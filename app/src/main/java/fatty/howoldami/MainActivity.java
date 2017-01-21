package fatty.howoldami;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(buttonClickedLister);

        ImageView bg3mao = (ImageView) findViewById(R.id.bg3mao);
        bg3mao.setVisibility(View.INVISIBLE);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private Button.OnClickListener buttonClickedLister = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button enterButton = (Button) findViewById(R.id.enterButton);
            ImageView bg3mao = (ImageView) findViewById(R.id.bg3mao);

            if (formatEditTextContext()) {
                bg3mao.setVisibility(View.INVISIBLE);
                enterButton.setText(R.string.normalEnter);
                calDateDiff();
            }else {
                enterButton.setText(R.string.errorEnter);
                bg3mao.setVisibility(View.VISIBLE);
            }
        }
    };

    private boolean formatEditTextContext() {       //格式化日期格式。缺字就補0。年不能超過現在時間

        try {

            EditText enterYear = (EditText) findViewById(R.id.yearTextFields);
            EditText enterMonth = (EditText) findViewById(R.id.monthTextField);
            EditText enterDate = (EditText) findViewById(R.id.dateTextField);
            String temp = "";

            Date now = new Date();
            Date date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");

            if (enterYear.getText().toString().length() < 4) {
                for (int i = 0; i < 4 - enterYear.getText().toString().length(); i++)
                    temp += "0";
                enterYear.setText(temp + enterYear.getText().toString());
            }
            if (enterMonth.getText().toString().length() == 1) {
                enterMonth.setText("0" + enterMonth.getText().toString());
            }
            if (enterDate.getText().toString().length() == 1) {
                enterDate.setText("0" + enterDate.getText().toString());
            }

            date = dateFormat.parse(enterYear.getText().toString() + enterMonth.getText().toString() + enterDate.getText().toString() + " " + "000000");

            if (date.before(now)
                    &&
                    (Integer.valueOf(enterMonth.getText().toString()) >= 1 &&
                            Integer.valueOf(enterMonth.getText().toString()) <= 12)
                    &&
                    (Integer.valueOf(enterDate.getText().toString()) >= 1 &&
                            Integer.valueOf(enterDate.getText().toString()) <= calMonthDays(Integer.valueOf(enterYear.getText().toString()), Integer.valueOf(enterMonth.getText().toString())))
                    ) {
            } else {
                printText("X","X","X","X","X","X","X","X","X","X");
                return false;
            }
            return true;

        } catch (Exception ex){
            printText("X","X","X","X","X","X","X","X","X","X");
            return false;
        }
    }

    private void calDateDiff() {

        EditText enterYear = (EditText) findViewById(R.id.yearTextFields);
        EditText enterMonth = (EditText) findViewById(R.id.monthTextField);
        EditText enterDate = (EditText) findViewById(R.id.dateTextField);

        long howOldYear = 0;
        long howOldMonth = 0;
        long howOldDate = 0;

        int MathematicsCarry = 0;   //紀錄剪法借位用

        long sec = 0;
        long min = 0;
        long hour = 0;
        long day = 0;
        long week = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date now = new Date();
        Date date = null;

        try {
            date = dateFormat.parse(enterYear.getText().toString() + enterMonth.getText().toString() + enterDate.getText().toString() + " " + "000000");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String timeString = dateFormat.format(now);
        String calString = dateFormat.format(date);

        if (((Integer.valueOf(timeString.substring(6, 8)) - Integer.valueOf(calString.substring(6, 8))) >= 0)) { //計算日

            howOldDate = (Integer.valueOf(timeString.substring(6, 8)) - Integer.valueOf(calString.substring(6, 8)));
        } else {

            howOldDate = (Integer.valueOf(timeString.substring(6, 8)) - Integer.valueOf(calString.substring(6, 8))) +
                    calMonthDays(Integer.valueOf(timeString.substring(6, 8)),Integer.valueOf(calString.substring(4,6)));
            MathematicsCarry = 1;
        }
        if( (Integer.valueOf(timeString.substring(4,6)) - Integer.valueOf(calString.substring(4,6))) - MathematicsCarry >= 0) {   //計算月

            howOldMonth = (Integer.valueOf(timeString.substring(4, 6)) - Integer.valueOf(calString.substring(4, 6))) - MathematicsCarry;
            MathematicsCarry = 0;
        }else {

            howOldMonth = (Integer.valueOf(timeString.substring(4, 6)) - Integer.valueOf(calString.substring(4, 6))) - MathematicsCarry + 12;
            MathematicsCarry = 1;
        }

        howOldYear = (Integer.valueOf(timeString.substring(0, 4)) - Integer.valueOf(calString.substring(0, 4))) - MathematicsCarry;

        sec = (now.getTime() - date.getTime()) / 1000;
        min = (long) Math.floor(sec / 60);
        hour = (long) Math.floor(min / 60);
        day = (long) Math.floor(hour / 24);
        week = (long) Math.floor(day / 7);

        printText(""+howOldYear,""+howOldMonth,""+howOldDate,""+howOldYear,""+(howOldYear*12 + howOldMonth),""+week,""+day,""+hour,""+min,""+sec);
    }
    private int calMonthDays(int year , int month){

        switch(month){

            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;

            case 4:
            case 6:
            case 9:
            case 11:
                return 30;

            case 2:
                if((    (year%400) == 0 ||
                        (year%4) == 0) && (year%100 != 0))
                    return 29;
                else
                    return 28;
            default:
                return 0;
        }
    }

    private void printText(String howOldYear,String howOldMonth,String howOldDate,String Year,String Month,String week,String day,String hour,String min,String sec){

        TextView textTotalYears = (TextView) findViewById(R.id.textTotalYears);
        TextView textTotalMonths = (TextView) findViewById(R.id.textTotalMonths);
        TextView textTotalWeeks = (TextView) findViewById(R.id.textTotalWeeks);
        TextView textTotalDayss = (TextView) findViewById(R.id.textTotalDays);
        TextView textTotalHours = (TextView) findViewById(R.id.textTotalHours);
        TextView textTotalMinutes = (TextView) findViewById(R.id.textTotalMinutes);
        TextView textTotalSeconds = (TextView) findViewById(R.id.textTotalSeconds);
        TextView howOldTextYears = (TextView) findViewById(R.id.howOldAmIYear);
        TextView howOldTextMonths = (TextView) findViewById(R.id.howOldAmIMonth);
        TextView howOldTextDate = (TextView) findViewById(R.id.howOldAmIDate);

        textTotalYears.setText("          " + Year);
        textTotalMonths.setText("          " + Month);
        textTotalWeeks.setText("          " + week);
        textTotalDayss.setText("          " + day);
        textTotalHours.setText("          " + hour);
        textTotalMinutes.setText("          " + min);
        textTotalSeconds.setText("          " + sec);

        howOldTextYears.setText("" + howOldYear);
        howOldTextMonths.setText("" + howOldMonth);
        howOldTextDate.setText("" + howOldDate);
    }
}
