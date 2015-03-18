namespace MyLocationServer.Models.Users {
    public class ErrorResponseModel : ResponseModel {
        public string Msg { get; set; }


        public ErrorResponseModel() {
            RC = 2;
        }
    }
}