import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Arrays;
import java.util.Comparator;

public class PositionMouseCursor {

    public static void main(String[] args) {
        int selectedMonitorIndex = ( args.length > 0 ) ? Integer.parseInt( args[0] ): 0;
        MonitorDimension[] monitors = getMonitorSizes();

        if ( selectedMonitorIndex >= monitors.length ) {
            System.err.println("Selected Monitor index is out of bounds.\nTotal monitors: " + monitors.length);
            System.exit(1);
        }
        
        // System.out.println( Arrays.toString( monitors ) );

        MonitorDimension selectedMonitor = monitors[ selectedMonitorIndex ];
        int x = selectedMonitor.x + (selectedMonitor.width / 2);
        int y = selectedMonitor.y + (selectedMonitor.height / 2 );
        setMousePosition( x, y );
    }


    public static MonitorDimension[] getMonitorSizes() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        int totalMonitors = gs.length;
        MonitorDimension[] monitors = new MonitorDimension[ totalMonitors ];
        for ( int i = 0; i < totalMonitors; i++ ) {
            DisplayMode dm = gs[i].getDisplayMode();
            Rectangle bounds = gs[i].getDefaultConfiguration().getBounds();
            monitors[ i ] = new MonitorDimension( dm.getWidth(), dm.getHeight(), bounds.x, bounds.y );
        }
        // Sorting to keep shortcuts the same based on monitor arrangement
        Arrays.sort( monitors, new SortByArrangement() );
        return monitors;
    }


    public static void setMousePosition( int x, int y ) {
        try {
            Robot robot = new Robot();
            robot.mouseMove( x, y );
        } catch (AWTException e) {
            System.out.println(e);
        }
    }


    private static class MonitorDimension {
        private int width = 0;
        private int height = 0;
        private int x = 0;
        private int y = 0;
        public MonitorDimension( int width, int height, int x, int y ) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            return sb.append( width )
                    .append( "x" )
                    .append( height )
                    .append(" (")
                    .append( x )
                    .append( "," )
                    .append( y )
                    .append(")")
                    .toString();
        }
    }
    
    private static class SortByArrangement implements Comparator<MonitorDimension> {
        public int compare( MonitorDimension a, MonitorDimension b ) {
            return a.x - b.x;
        }
    }
}
