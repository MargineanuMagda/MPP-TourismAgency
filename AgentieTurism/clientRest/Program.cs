using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using model.domain;
using System.Web.Helpers;

namespace clientRest
{

    class Program
    {
        static HttpClient client = new HttpClient();

        public static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            RunAsync().Wait();
        }

        static async Task RunAsync()
        {
            client.BaseAddress = new Uri("http://localhost:8080/agency/trips");
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            String resultAll = await GetTextAsync("http://localhost:8080/agency/trips/");
            Console.WriteLine("Get All Probe: \n{0}", resultAll);

            String id = "2";
            Trip result = await GetTripAsync("http://localhost:8080/agency/trips/" + id);
            Console.WriteLine("Get trip by id {0}", result);
            
            //Trip p = await PostAsync("http://localhost:8080/agency/trips/");
            //Console.WriteLine("Add: Trip " + p);
            
            String idToUpdate = "3";
            Trip pr = await PutAsync("http://localhost:8080/agency/trips/" + idToUpdate);
            Console.WriteLine("Update: Trip " + pr);
/*
            String idToDel = "12";
            bool response = await DeleteAsync("http://localhost:8080/agency/trips/" + idToDel);
            Console.WriteLine("Delete Trip cu id " + idToDel);*/
            

        }
        static async Task<String> GetTextAsync(string path)
        {
            String product = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsStringAsync();
            }
            return product;
        }
        static async Task<Trip> GetTripAsync(string path)
        {
            Trip product = null;
            HttpResponseMessage response = await client.GetAsync(path);
            if (response.IsSuccessStatusCode)
            {
                String s=await response.Content.ReadAsStringAsync();
                Console.WriteLine(s);
                product = JsonConvert.DeserializeObject<Trip>(s);
            }
            return product;
        }
        static async Task<Trip> PostAsync(string path)
        {
            Trip product = null;
            
            string json = @"{   'id: 0,
                                'place': 'BrasovC#',
                                'transport': 'EliTrans',
                                'date': '2021-06-26 22:02',
                                'price': 400,
                                'nrTickets': 120,
                                'freeTickets': 100}";
            Trip p = JsonConvert.DeserializeObject<Trip>(json);
            Console.WriteLine("Trip to add: "+p);
            HttpResponseMessage response = await client.PostAsJsonAsync(path, p);
            if (response.IsSuccessStatusCode)
            {
                String s = await response.Content.ReadAsStringAsync();
                product = JsonConvert.DeserializeObject<Trip>(s);
            }
            return product;
        }
        static async Task<bool> DeleteAsync(string path)
        {
            HttpWebResponse product = null;
            /*string jsonData = @"{ 'id':'25', 'numeProba':'Update','tipProba':'Update'}";
			Proba p = JsonConvert.DeserializeObject<Proba>(jsonData);*/
            HttpResponseMessage response = await client.DeleteAsync(path);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsAsync<HttpWebResponse>();
            }
            return product != null;
        }
        static async Task<Trip> PutAsync(string path)
        {
            Trip product = null;
            Trip newTrip = new Trip("BrasovUpdateC#", "EliTrans", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"), 400, 300);
            newTrip.ID = 3;
            string json = JsonConvert.SerializeObject(newTrip);
            
            Console.WriteLine("Json generat: \n"+json);
            string jsonData = @"{   'id':3,
                                'place': 'BrasovUpdateC#',
                                'transport': 'EliTrans',
                                'date': '2021-06-26 22:02',
                                'price': 400,
                                'nrTickets': 120,
                                'freeTickets': 100}";
            Trip p = JsonConvert.DeserializeObject<Trip>(jsonData);
            Console.WriteLine("Trip deserializat: "+p);
            HttpContent inputContent = new StringContent(json, Encoding.UTF8, "application/json");
            HttpResponseMessage response = client.PutAsync(path, inputContent).Result;
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsAsync<Trip>();
            }
            return product;
        }
    }
}
