package com.tag.timezoneex;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Spinner spinnerAvailableID;
	private Calendar current;
	private TextView textTimeZone, txtCurrentTime, txtTimeZoneTime;
	private long miliSeconds;
	private ArrayAdapter<String> idAdapter;
	private SimpleDateFormat sdf;
	private Date resultdate;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		spinnerAvailableID = (Spinner) findViewById(R.id.availableID);

		textTimeZone = (TextView) findViewById(R.id.timezone);
		txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
		txtTimeZoneTime = (TextView) findViewById(R.id.txtTimeZoneTime);

		String[] idArray = TimeZone.getAvailableIDs();

		sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");

		idAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, idArray);

		idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAvailableID.setAdapter(idAdapter);

		getGMTTime();

		spinnerAvailableID
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						getGMTTime();
						String selectedId = (String) (parent
								.getItemAtPosition(position));

						TimeZone timezone = TimeZone.getTimeZone(selectedId);
						String TimeZoneName = timezone.getDisplayName();

						int TimeZoneOffset = timezone.getRawOffset()
								/ (60 * 1000);

						int hrs = TimeZoneOffset / 60;
						int mins = TimeZoneOffset % 60;

						miliSeconds = miliSeconds + timezone.getRawOffset();

						resultdate = new Date(miliSeconds);
						System.out.println(sdf.format(resultdate));

						textTimeZone.setText(TimeZoneName + " : GMT " + hrs + "."
								+ mins);
						txtTimeZoneTime.setText("" + sdf.format(resultdate));
						miliSeconds = 0;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}
	
	// Convert Local Time into GMT time
	
	private void getGMTTime() {
		current = Calendar.getInstance();
		txtCurrentTime.setText("" + current.getTime());

		miliSeconds = current.getTimeInMillis();

		TimeZone tzCurrent = current.getTimeZone();
		int offset = tzCurrent.getRawOffset();
		if (tzCurrent.inDaylightTime(new Date())) {
			offset = offset + tzCurrent.getDSTSavings();
		}

		miliSeconds = miliSeconds - offset;

		resultdate = new Date(miliSeconds);
		System.out.println(sdf.format(resultdate));
	}
}
