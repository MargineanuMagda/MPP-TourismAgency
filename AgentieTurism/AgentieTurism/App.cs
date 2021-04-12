using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using AgentieTurism.service;
using AgentieTurism.domain;

namespace AgentieTurism
{
    partial class App : Form
    {
        ServiceAgency service;
        TravelAgent mainUser;
        public App(ServiceAgency service)
        {
            this.service = service;
            InitializeComponent();
            InitTables();
        }
        private void InitTables()
        {
            var source = new BindingSource();
            source.DataSource = service.GetAllTrips().ToList();
            table1.AutoGenerateColumns = true;
            table1.DataSource = source;
            foreach (DataGridViewRow Myrow in table1.Rows)
            {            
                if (Convert.ToInt32(Myrow.Cells[5].Value) ==0)
                {
                    /*Myrow.DefaultCellStyle.BackColor = Color.Red;*/
                    Myrow.DefaultCellStyle.BackColor = Color.FromKnownColor(KnownColor.Red);
                    Myrow.ReadOnly = true;
                }
                
            }
        }

        private void searchBtn_Click(object sender, EventArgs e)
        {
            string name = "";
            if(checkBox1.Checked==true)
                name = nameBoxT2.Text;
            int minHour = 0;
            if (checkBox2.Checked == true)
                minHour = (int)numericUpDownMIN.Value;
            int maxHour =23;
            if (checkBox2.Checked == true)
                maxHour = (int)numericUpDownMAX.Value;
            Console.WriteLine("name={0}, min={1}, max={2}",name,minHour,maxHour);
            var source = new BindingSource();
            source.DataSource= service.FindTripsByNameAndHours(name, minHour, maxHour);
            table2.DataSource = source;

        }

        private void reserveBtnT2_Click(object sender, EventArgs e)
        {
            if (table2.SelectedRows.Count > 0)
            {
                Trip t = (Trip)table2.CurrentRow.DataBoundItem;
                Console.WriteLine(t);
                placeInfo.Text = t.Place;
                transInfo.Text = t.Transport;
                priceInfo.Text = t.Price.ToString();
                dateInfo.Text = t.Data.ToString();
                TripName.Text = t.ToString();
                tabControl1.SelectedIndex = 2;
            }
            else
            {
                MessageBox.Show("Please select a trip first!!");
            }
        }

        private void numericUpDown1_ValueChanged(object sender, EventArgs e)
        {
            Trip t = (Trip)table2.CurrentRow.DataBoundItem;
            double price = (int)numericUpDown1.Value * t.Price;
            priceLbl.Text = price.ToString();
        }

        private void reserveBtnT3_Click(object sender, EventArgs e)
        {
            Trip t = (Trip)table2.CurrentRow.DataBoundItem;
            string client = nameRes.Text;
            string tel = telRes.Text;
            int nrTick = (int)numericUpDown1.Value;
            Console.WriteLine("Nr of tickets: {0}",nrTick);
            if(client=="" || tel =="" || nrTick == 0)
            {
                MessageBox.Show("All fields are required!");
            }
            else
            {
                if (t.FreeTickets < nrTick)
                {
                    MessageBox.Show("There are no tickets avaible!");
                }
                else
                {
                    
                    
                    service.AddReservation(client, tel, t, nrTick);
                    InitTables();
                }
            }
        }

        private void logoutBtn_Click(object sender, EventArgs e)
        {
            service.Logout();
            this.Hide();
        }
    }
}
