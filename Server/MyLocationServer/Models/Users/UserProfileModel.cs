namespace MyLocationServer.Models.Users {
    public class UserProfileModel : ResponseModel {
        public string Username { get; set; }
        public string PassHash { get; set; }
    }
}