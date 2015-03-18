using System;

namespace MyLocationServer.Models.Geo {
    public class CoordModel : ResponseAuthorizedModel {
        public Coord Coords { get; set; }
        public DateTime Time { get; set; }

        public CoordModel() {
            Coords = new Coord();
        }
    }
}