package at.allaboutapps.recyclerview.decorations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Loops through the values passed in and draws a background behind the views. Pass in {@code 0} to
 * draw nothing.
 * <p>
 * <pre>{@code
 * recyclerView.addItemDecoration(new A3AlternatingBackgroundDecoration(0, Color.RED));
 * }</pre>
 */
public class A3AlternatingBackgroundDecoration extends RecyclerView.ItemDecoration {

  /**
   * Constant for not drawing behind every nth view.
   */
  public static final int COLOR_NONE = 0;

  private final Paint mPaint;
  private final int[] mColors;

  /**
   * Draws a background behind the views, cycling through the colors passed in.
   *
   * @param colors a list of colors, or {@link #COLOR_NONE} for no background.
   */
  public A3AlternatingBackgroundDecoration(@ColorInt int... colors) {
    mColors = colors;
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);

    final int childCount = parent.getChildCount();
    final int colorCount = mColors.length;

    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.ViewHolder holder = parent.getChildViewHolder(child);

      final int position = holder.getAdapterPosition();
      final int sanitizedPosition = position < 0 ? holder.getOldPosition() : position;

      final int color = mColors[sanitizedPosition % colorCount];

      if (color == COLOR_NONE) {
        continue;
      }

      final Paint paint = mPaint;
      paint.setColor(color);

      c.drawRect(
          child.getLeft() + child.getTranslationX(),
          child.getTop() + child.getTranslationY(),
          child.getRight() + child.getTranslationX(),
          child.getBottom() + child.getTranslationY(),
          paint);
    }
  }
}
