namespace MyLocationServer.Models {
    public class ResponseAuthorizedModel : ResponseModel {
        public string UID { get; set; }

        public ResponseAuthorizedModel() {
            RC = 0;
        }
    }
}