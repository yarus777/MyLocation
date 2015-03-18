using System;
using System.Linq;
using System.Web.Mvc;
using MyLocationServer.Models;
using MyLocationServer.Models.Geo;
using MyLocationServer.Models.Users;

namespace MyLocationServer.Controllers {
    public class GeoController : PostJsonController {
        [HttpPost]
        public ActionResult Set() {
            try {
                using (var context = new LocationContext()) {
                    var obj = GetEventParameter<CoordModel>();
                    var user = context.Users.FirstOrDefault(u => u.UID == obj.UID);
                    if (user == null) {
                        return Json(new ResponseModel {RC = 1});
                    }
                    context.Coords.Add(new Coord {
                        Lattitude = obj.Coords.Lattitude,
                        Longitude = obj.Coords.Longitude,
                        Time = obj.Time,
                        UserId = user.Id
                    });
                    context.SaveChanges();
                    return Json(new CoordModel {Time = obj.Time, Coords = obj.Coords});
                }
            }
            catch (Exception e) {
                return Json(new ErrorResponseModel { Msg = e.Message });
            }
        }

        [HttpPost]
        public ActionResult Get() {
            try {
                using (var context = new LocationContext()) {
                    var obj = GetEventParameter<CoordModel>();
                    var user = context.Users.FirstOrDefault(u => u.UID == obj.UID);
                    if (user == null) {
                        return Json(new ResponseModel { RC = 1 });
                    }
                    var lastCoord = context.Coords.FirstOrDefault(x => x.UserId == user.Id);
                    if (lastCoord == null) {
                        return Json(new CoordModel {RC = 0});
                    }
                    return Json(new CoordModel {
                        Coords = new Models.Geo.Coord {
                            Lattitude = lastCoord.Lattitude,
                            Longitude = lastCoord.Longitude
                        },
                        Time = lastCoord.Time,
                        UID = user.UID,
                        RC = 0
                    });
                }
            }
            catch (Exception e) {
                return Json(new ResponseModel { RC = 2 });
            }
        }

        [HttpPost]
        public ActionResult Index() {
            return Get();
        }
    }
}
