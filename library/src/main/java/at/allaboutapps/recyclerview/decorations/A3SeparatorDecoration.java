package at.allaboutapps.recyclerview.decorations;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Draws a separator beneath the views, omitting the last.
 *
 * <pre>{@code
 * recyclerView.addItemDecoration(new A3SeparatorDecoration(getResources(), Color.BLUE));
 * }</pre>
 */
public class A3SeparatorDecoration extends RecyclerView.ItemDecoration {

  private final Paint mPaint;
  private final int mStrokeWidth;
  private int mPaddingLeft;
  private int mPaddingRight;
  private DisplayMetrics mDisplayMetrics;

  /**
   * Separator with a 1dp stroke.
   *
   * @param resources the application resources
   * @param color the color to use
   */
  public A3SeparatorDecoration(@NonNull Resources resources, @ColorInt int color) {
    this(resources, color, 1);
  }

  /**
   * A Separator.
   *
   * @param resources the application resources
   * @param color the color to use
   * @param widthDp the width of the separator
   */
  public A3SeparatorDecoration(
      @NonNull Resources resources,
      @ColorInt int color,
      @FloatRange(from = 0f, fromInclusive = false) float widthDp) {
    mPaint = new Paint();
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(color);

    mDisplayMetrics = resources.getDisplayMetrics();
    mStrokeWidth = (int) getPixelFromDp(widthDp);
  }

  private float getPixelFromDp(float dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics);
  }

  /**
   * Padding in dp left and right of the separator.
   *
   * @param padding the padding in dp
   */
  public void setPadding(@IntRange(from = 0) int padding) {
    setPadding(padding, padding);
  }

  /**
   * Padding in dp left and right of the separator.
   *
   * @param paddingLeft the padding left in dp
   * @param paddingRight the padding right in dp
   */
  public void setPadding(
      @IntRange(from = 0) int paddingLeft, @IntRange(from = 0) int paddingRight) {
    mPaddingLeft = (int) getPixelFromDp(paddingLeft);
    mPaddingRight = (int) getPixelFromDp(paddingRight);
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);

      if (isLast(child, parent, state)) {
        continue;
      }

      c.drawRect(
          child.getLeft() + child.getTranslationX() + mPaddingLeft,
          child.getBottom() + child.getTranslationY(),
          child.getRight() - mPaddingRight + child.getTranslationX(),
          child.getBottom() + child.getTranslationY() + mStrokeWidth,
          mPaint);
    }
  }

  private boolean isLast(View child, RecyclerView parent, RecyclerView.State state) {
    return parent.getChildAdapterPosition(child) == state.getItemCount() - 1;
  }

  @Override
  public void getItemOffsets(
      Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    if (isLast(view, parent, state)) {
      outRect.set(0, 0, 0, 0);
    } else {
      outRect.set(0, 0, 0, mStrokeWidth);
    }
  }
}
