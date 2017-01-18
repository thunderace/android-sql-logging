package io.explod.android.sqllog.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import io.explod.android.sqllog.sample.R;
import io.explod.android.sqllog.ui.activity.LogViewerActivity;
import timber.log.Timber;
import io.explod.android.sqllog.data.LogEntry;
import io.explod.android.sqllog.data.LogEntryProvider;


public class MainActivity extends AppCompatActivity {

	private static final String TAG = LogViewerActivity.class.getSimpleName();

	EditText mMessageText;

	CheckBox mGenerateExceptionCheckBox;
	CheckBox mUseTimberCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMessageText = (EditText) findViewById(R.id.edit_message);
		mGenerateExceptionCheckBox = (CheckBox) findViewById(R.id.check_add_exception);
		mUseTimberCheckBox = (CheckBox) findViewById(R.id.check_use_timber);

		findViewById(R.id.button_generate_verbose).setOnClickListener(onClickLogMessage(Log.VERBOSE));
		findViewById(R.id.button_generate_debug).setOnClickListener(onClickLogMessage(Log.DEBUG));
		findViewById(R.id.button_generate_info).setOnClickListener(onClickLogMessage(Log.INFO));
		findViewById(R.id.button_generate_warn).setOnClickListener(onClickLogMessage(Log.WARN));
		findViewById(R.id.button_generate_error).setOnClickListener(onClickLogMessage(Log.ERROR));
		findViewById(R.id.button_generate_assert).setOnClickListener(onClickLogMessage(Log.ASSERT));

		findViewById(R.id.button_view_logs).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToLogViewer();
			}
		});
		
	}

	private View.OnClickListener onClickLogMessage(final int priority) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				generateLogEntry(priority);
			}
		};
	}

	private void generateLogEntry(int priority) {
		String message = mMessageText.getText().toString();
		boolean logException = mGenerateExceptionCheckBox.isChecked();

		if (TextUtils.isEmpty(message) && !logException) {
			Toast.makeText(this, R.string.error_need_message_or_exception, Toast.LENGTH_SHORT).show();
			return;
		}

		if (logException) {
			Exception sample = new Exception("sample exception");
			if (mUseTimberCheckBox.isChecked()) {
				Timber.tag(TAG).log(priority, sample, message);
			} else {
				LogEntryProvider.insertLogEntry(this, LogEntry.create(priority, TAG, message, sample));
			}
		} else {
			if (mUseTimberCheckBox.isChecked()) {
				Timber.tag(TAG).log(priority, message);
			} else {
				LogEntryProvider.insertLogEntry(this, priority, TAG, message);
			}
		}
	}

	private void goToLogViewer() {
		Intent intent = new Intent(this, LogViewerActivity.class);
		startActivity(intent);
	}

}
