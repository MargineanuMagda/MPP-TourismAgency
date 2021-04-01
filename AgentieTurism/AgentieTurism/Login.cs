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
    partial class Login : Form
    {
        ServiceAgency service;
        public Login(ServiceAgency serv)
        {
            InitializeComponent();
            service=serv;

        }

        
        

        private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {

        }

        private void button3_Click(object sender, EventArgs e)
        {
            string user = textBox5.Text;
            string pass1 = textBox6.Text;
            string pass2 = textBox7.Text;
            if (pass1 != pass2)
            {
                MessageBox.Show("Bouth passwords should be the same!");
            }
            else
            {
                TravelAgent agent = new TravelAgent(user, pass1);
                service.AddUser(agent);
                service.Login(user, pass1);
                
                App app = new App(service);
                app.ShowDialog();

            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string user = textBox4.Text;
            string passw = textBox3.Text;
            try
            {
                service.Login(user, passw);
                App app = new App(service);
                app.ShowDialog();

            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void linkLabel2_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
        {
            tabControl1.SelectedIndex = 1;
        }
    }
}
