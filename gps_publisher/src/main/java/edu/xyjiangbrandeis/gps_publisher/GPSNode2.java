//package edu.xyjiangbrandeis.gps_publisher;
//
//import android.hardware.Camera;
//import android.hardware.Camera.PreviewCallback;
//import android.hardware.Camera.Size;
//
//import com.google.common.base.Preconditions;
//
//import org.ros.namespace.GraphName;
//import org.ros.namespace.NameResolver;
//import org.ros.node.ConnectedNode;
//import org.ros.node.DefaultNodeFactory;
//import org.ros.node.Node;
//import org.ros.node.NodeConfiguration;
//import org.ros.node.NodeMain;
//import org.ros.node.topic.Publisher;
//
//import java.util.concurrent.Executors;
//
////import org.ros.message.Time;
////import org.ros.message.sensor_msgs.CameraInfo;
////import org.ros.message.sensor_msgs.CompressedImage;
//
///**
// * @author damonkohler@google.com (Damon Kohler)
// */
//public class GPSNode2 implements NodeMain {
//
//    private Node node;
//    private Publisher<std_msgs.Float64MultiArray> publisher;
//
//    @Override
//    public GraphName getDefaultNodeName() {
//        return null;
//    }
//
//    @Override
//    public void onStart(ConnectedNode connectedNode) {
//
//    }
//
//    @Override
//    public void onShutdown(Node node) {
//
//    }
//
//    @Override
//    public void onShutdownComplete(Node node) {
//
//    }
//
//    @Override
//    public void onError(Node node, Throwable throwable) {
//
//    }
//
//
//
//    private final class PublishingPreviewCallback implements PreviewCallback {
//        @Override
//        public void onPreviewFrame(byte[] data, Camera camera) {
//            CompressedImage image = new CompressedImage();
//            CameraInfo cameraInfo = new CameraInfo();
//            String frameId = "camera";
//
//            // TODO(ethan): Right now serialization is deferred. When serialization
//            // happens inline, we won't need to copy.
//            image.data = new byte[data.length];
//            System.arraycopy(data, 0, image.data, 0, data.length);
//
//            image.format = "jpeg";
//            //image.header.stamp = Time.fromMillis(System.currentTimeMillis());
//            image.header.frame_id = frameId;
//            imagePublisher.publish(image);
//
//            //cameraInfo.header.stamp = image.header.stamp;
//            cameraInfo.header.frame_id = frameId;
//
//            Size previewSize = camera.getParameters().getPreviewSize();
//            cameraInfo.width = previewSize.width;
//            cameraInfo.height = previewSize.height;
//            cameraInfoPublisher.publish(cameraInfo);
//        }
//    }
//
//
//    @Override
//    public void main(NodeConfiguration nodeConfiguration) throws Exception {
//        Preconditions.checkState(node == null);
//        node = new DefaultNodeFactory(Executors.newScheduledThreadPool(5)).newNode(nodeConfiguration);
//        NameResolver resolver = node.getResolver().newChild("android/camera");
//        publisher =
//                node.newPublisher(resolver.resolve("image_raw"), "sensor_msgs/CompressedImage");
//        cameraInfoPublisher =
//                node.newPublisher(resolver.resolve("camera_info"), "sensor_msgs/CameraInfo");
//        setPreviewCallback(new PublishingPreviewCallback());
//    }
//
//    @Override
//    public void shutdown() {
//        if (node != null) {
//            node.shutdown();
//            node = null;
//        }
//        releaseCamera();
//    }
//
//}
