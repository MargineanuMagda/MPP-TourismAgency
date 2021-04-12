using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace networking.dto
{
    [Serializable]
    public class FilterDTO
    {
        public string Place { get; set; }
        public int MinHour { get; set; }
        public int MaxHour { get; set; }

        public FilterDTO(string place, int minHour, int maxHour)
        {
            Place = place;
            MinHour = minHour;
            MaxHour = maxHour;
        }
    }
}
