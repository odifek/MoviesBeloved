package com.techbeloved.moviesbeloved.moviedetails;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import com.techbeloved.moviesbeloved.R;

/**
 * Adds an icon to a text view and limits it to number of lines given by MAX_LINES
 * When the icon is clicked, the textview expands and vice versa
 * Copied from https://stackoverflow.com/a/22751079/3884184
 */
public class ExpandableTextView extends AppCompatTextView implements View.OnClickListener
{
    private static final int MAX_LINES = 5;
    private int currentMaxLines = Integer.MAX_VALUE;

    public ExpandableTextView(Context context)
    {
        super(context);
        setOnClickListener(this);
    }
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter)
    {
        /* If text longer than MAX_LINES set DrawableBottom - I'm using '...' icon */
        post(new Runnable()
        {
            public void run()
            {
                if (getLineCount()>MAX_LINES)
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.icon_more_text);
                else
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                setMaxLines(MAX_LINES);
            }
        });
    }


    @Override
    public void setMaxLines(int maxLines)
    {
        currentMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /* Custom method because standard getMaxLines() requires API > 16 */
    public int getMyMaxLines()
    {
        return currentMaxLines;
    }

    @Override
    public void onClick(View v)
    {
        /* Toggle between expanded collapsed states */
        if (getMyMaxLines() == Integer.MAX_VALUE)
            setMaxLines(MAX_LINES);
        else
            setMaxLines(Integer.MAX_VALUE);
    }

}