using System;
using System.Linq;
using System.Web.Mvc;
using MyLocationServer.Models;
using MyLocationServer.Models.Users;

namespace MyLocationServer.Controllers {
    public class AuthController : PostJsonController {
        [HttpPost]
        public ActionResult Login() {
            try {
                using (var context = new LocationContext()) {
                    var userObj = GetEventParameter<UserProfileModel>();
                    var existingUser =
                        context.Users.FirstOrDefault(
                            user => user.Username == userObj.Username && user.PasswordHash == userObj.PassHash);
                    if (existingUser == null) {
                        return Json(new ResponseModel {RC = 1});
                    }
                    existingUser.UID = Guid.NewGuid().ToString();
                    context.SaveChanges();
                    return Json(new ResponseAuthorizedModel {UID = existingUser.UID});
                }
            }
            catch (Exception e) {
                return Json(new ErrorResponseModel {Msg = e.Message});
            }
        }

        [HttpPost]
        public ActionResult Index() {
            return Login();
        }
    }
}
