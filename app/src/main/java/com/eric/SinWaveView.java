package com.eric;

/**
 * @Author: chen
 * @datetime: 2024/1/20
 * @desc:
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SinWaveView extends View {
   private Paint paint;
   private float amplitude = 100; // 振幅
   private float frequency = 0.02f; // 频率
   private float phase = 0; // 相位
   private float xOffset = 0;

   public SinWaveView(Context context) {
      super(context);
      init();
   }

   public SinWaveView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
   }

   private void init() {
      paint = new Paint();
      paint.setColor(Color.BLUE);
      paint.setStrokeWidth(5);
      paint.setAntiAlias(true);
   }

   @Override
   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);

      int width = getWidth();
      int height = getHeight();

      float centerY = height / 2f;

      // 绘制 sin 函数的波形
      for (float x = 0; x < width; x++) {
         float y = (float) (amplitude * Math.sin(frequency * x + phase) + centerY);
         canvas.drawPoint(x, y, paint);
      }

      // 更新相位，实现动画效果
      phase += 0.1;
      invalidate(); // 触发重绘
   }
}
