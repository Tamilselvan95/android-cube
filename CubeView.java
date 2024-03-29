package com.codentronix.android;
 
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
 
public class CubeView extends View {
    /* Vertices of the cube. */
    protected Vector3D vertices[];
 
    /* Define the indices to the vertices of each face of the cube. */
    protected int faces[][];
 
    /* Colors of each face of the cube. */
    protected int colors[];
 
    /* Orientation of the cube around X axis. */
    protected float ax;
 
    /* Orientation of the cube around Y axis. */
    protected float ay;
 
    /* Orientation of the cube around Z axis. */
    protected float az;
 
    protected float lastTouchX;
    protected float lastTouchY;
 
    /* This constructor is used when the view is created from code. */
    public CubeView(Context context) {
        super(context);
    }
 
    /* This constructor is used when the view is inflated from a XML file. */
    public CubeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    /**
     * Initializes the cube geometry.
     */
    public void initialize() {
        vertices = new Vector3D[] {
            new Vector3D(-1, 1, -1),
            new Vector3D(1, 1, -1),
            new Vector3D(1, -1, -1),
            new Vector3D(-1, -1, -1),
            new Vector3D(-1, 1, 1),
            new Vector3D(1, 1, 1),
            new Vector3D(1, -1, 1),
            new Vector3D(-1, -1, 1)
        };
 
        // Define the 6 faces of the cube. We specify the indices to the 4 vertices of each face.
        faces = new int[][] {{0, 1, 2, 3}, {1, 5, 6, 2}, {5, 4, 7, 6}, {4, 0, 3, 7}, {0, 4, 5, 1}, {3, 2, 6, 7}};
 
        // Define the color of each face.
        colors = new int[] {Color.BLUE, Color.RED, Color.YELLOW, Color.LTGRAY, Color.CYAN, Color.MAGENTA};
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
 
        // Initialize the cube geometry.
        initialize();
 
        // Allow the view to receive touch input.
        setFocusableInTouchMode(true);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ) {
            lastTouchX = event.getX();
            lastTouchY = event.getY();
        }else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
            float dx = (event.getX() - lastTouchX) / 30.0f;
            float dy = (event.getY() - lastTouchY) / 30.0f;
            ax += dy;
            ay -= dx;
            postInvalidate();
        }
        return true;
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        Vector3D t[]  = new Vector3D[8];
        double avgZ[] = new double[6];
        int order[] = new int[6];
 
        for( int i = 0; i < 8; i++ ) {
            // Rotate the vertex around X, next around Y, and then around Z.
            t[i] = vertices[i].rotateX(ax).rotateY(ay).rotateZ(az);
 
            // Finally, map the vertex from 3D to 2D.
            t[i] = t[i].project(getWidth(), getHeight(), 256, 4);
        }
 
        // Compute the average Z value of each face.
        for( int i = 0; i < 6; i++ ) {
            avgZ[i] = (t[faces[i][0]].z + t[faces[i][1]].z + t[faces[i][2]].z + t[faces[i][3]].z) / 4;
            order[i] = i;
        }
 
        // Next we sort the faces in descending order based on the Z value.
        // The objective is to draw distant faces first. This is called
        // the PAINTERS ALGORITHM. So, the visible faces will hide the invisible ones.
        // The sorting algorithm used is the SELECTION SORT.
        for( int i = 0; i             int iMax = i;
            for( int j = i + 1; j  avgZ[iMax] ) {
                    iMax = j;
                }
            }
            if( iMax != i ) {
                double dTmp = avgZ[i];
                avgZ[i] = avgZ[iMax];
                avgZ[iMax] = dTmp;
 
                int iTmp = order[i];
                order[i] = order[iMax];
                order[iMax] = iTmp;
            }
        }
 
        canvas.drawColor(Color.BLACK);
 
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL);
 
        paint.setTextSize(24);
        canvas.drawText("3D Cube",10,40,paint);
 
        paint.setTextSize(12);
        canvas.drawText("Drag the cube to change its orientation", 10, 80, paint);
        canvas.drawText("tamil", 10, getHeight() - 10, paint);
 
        for( int i = 0; i < 6; i++ ) {
            int index = order[i];
 
            Path p = new Path();
            p.moveTo((float)t[faces[index][0]].x, (float)t[faces[index][0]].y);
            p.lineTo((float)t[faces[index][1]].x, (float)t[faces[index][1]].y);
            p.lineTo((float)t[faces[index][2]].x, (float)t[faces[index][2]].y);
            p.lineTo((float)t[faces[index][3]].x, (float)t[faces[index][3]].y);
            p.close();
 
            paint.setColor(colors[index]);
            canvas.drawPath(p, paint);
        }
    }
}