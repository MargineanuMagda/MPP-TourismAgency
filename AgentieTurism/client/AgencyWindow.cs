using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;
using model.domain;
namespace client
{
    partial class AgencyWindow : Form
    {
        
        TravelAgent mainUser;
        ClientController ctrl;
        IList<Trip> allTrips;
        IList<Trip> _filteredTrips=new List<Trip>();

        public AgencyWindow(ClientController ctrl)
        {
            this.ctrl = ctrl;
            ctrl.updateEvent += userUpdate;
            InitializeComponent();
            allTrips = ctrl.GetAllTrips().ToList();
            InitTables();
        }

        private void userUpdate(object sender, AgencyUserEventArgs e)
        {
            if (e.UserEventType == AgencyUserEvent.ReservationAdded)
            {
                
                Reservation r = (Reservation)e.Data;
                Console.WriteLine("Update in curs");
                foreach (Trip trip in allTrips)
                {
                    if (trip.ID == r.Trip.ID)
                    {
                        trip.FreeTickets -= r.NrTickets;
                        Console.WriteLine("modificare facuta");
                    }
                }

                Console.WriteLine("Filtered Trips: ");
                Console.WriteLine(_filteredTrips);
                if (_filteredTrips.Count > 0)
                {
                    foreach (Trip trip in _filteredTrips)
                                    {
                                        if (trip.ID == r.Trip.ID)
                                        {
                                            trip.FreeTickets -= r.NrTickets;
                                            Console.WriteLine("modificare facuta");
                                        }
                                    }
                }
                
                Console.WriteLine("Refresh");
                /*table1.BeginInvoke((Action)delegate { table1.DataSource = allTrips; });
                table2.BeginInvoke((Action)delegate { table2.DataSource = filteredTrips; });*/
                table1.BeginInvoke((Action)delegate
                {
                    table1.DataSource = null;
                    table1.DataSource = allTrips;  });
                table2.BeginInvoke((Action)delegate
                {
                    table2.DataSource = null;
                    table2.DataSource = _filteredTrips; });
                    
                

            }
        }

        private void AgencytWindow_FormClosing(object sender, FormClosingEventArgs e)
        {
            Console.WriteLine("AgencyWindow closing " + e.CloseReason);
            if (e.CloseReason == CloseReason.UserClosing)
            {
                ctrl.Logout();
                ctrl.updateEvent -= userUpdate;
                Application.Exit();
            }

        }
        private void InitTables()
        {
            var source = new BindingSource();
            
            source.DataSource = allTrips;
            table1.AutoGenerateColumns = true;
            table1.DataSource = source;
            
            foreach (DataGridViewRow myrow in table1.Rows)
            {            
                //Console.WriteLine(Myrow.Cells[5].Value);
                if (Convert.ToInt32(myrow.Cells[5].Value) ==0)
                {
                  //  Console.WriteLine("DA");
                    myrow.DefaultCellStyle.BackColor = Color.FromKnownColor(KnownColor.Red);
                    myrow.ReadOnly = true;
                }
                
            }
            table1.Refresh();
            table1.CellFormatting +=
                new System.Windows.Forms.DataGridViewCellFormattingEventHandler(
                    this.dataGridView1_CellFormatting);

            var source2 = new BindingSource();
            source2.DataSource = _filteredTrips;
            table2.DataSource = source2;
        }

        private void dataGridView1_CellFormatting(object sender,
            System.Windows.Forms.DataGridViewCellFormattingEventArgs e)
        {
            //Console.WriteLine(table1.Columns[e.ColumnIndex].Name);
            // Set the background to red for negative values in the Balance column.
            if (e.ColumnIndex==5 && e.Value != null)
            {
                
                Int32 intValue=Convert.ToInt32(e.Value);
                if (intValue==0)
                {
                    
                    table1.Rows[e.RowIndex].DefaultCellStyle.BackColor = Color.Red;
                    table1.Rows[e.RowIndex].DefaultCellStyle.ForeColor=Color.Black;
                    //e.CellStyle.SelectionBackColor = Color.DarkRed;
                }
            }
        }

        private void searchBtn_Click(object sender, EventArgs e)
        {
            string name = "";
            if(checkBox1.Checked)
                name = nameBoxT2.Text;
            int minHour = 0;
            if (checkBox2.Checked)
                minHour = (int)numericUpDownMIN.Value;
            int maxHour =23;
            if (checkBox2.Checked)
                maxHour = (int)numericUpDownMAX.Value;
            Console.WriteLine("name={0}, min={1}, max={2}",name,minHour,maxHour);
            

            _filteredTrips = ctrl.FindTripsByNameAndHours(name, minHour, maxHour).ToList();
            var source = new BindingSource();
            source.DataSource = _filteredTrips;
            table2.DataSource = source;
            table2.Refresh();

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
                dateInfo.Text = t.Date.ToString();
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
            if (table2.CurrentRow != null)
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

                    
                        ctrl.AddReservation(client, tel, t, nrTick);
                        MessageBox.Show("Reservation added succesfully!");
                        //InitTables();
                    }
                }
            }
            else
            {
                MessageBox.Show("No trip selected!!");
            }
        }

        private void logoutBtn_Click(object sender, EventArgs e)
        {
            
            ctrl.Logout();
            ctrl.updateEvent -= userUpdate;
            Application.Exit();
        }
    }
}
