import React from  'react';
import './App'

import TripForm from "./TripForm"

class TripRow extends React.Component{

    render() {
        return (
            <tr>
                <td>{this.props.trip.id}</td>
                <td>{this.props.trip.place}</td>
                <td>{this.props.trip.transport}</td>
                <td>{this.props.trip.date}</td>
                <td>{this.props.trip.price}</td>
                <td>{this.props.trip.nrTickets}</td>
                <td>{this.props.trip.freeTickets}</td>
            </tr>
        );
    }
}
class TripTable extends React.Component{

    render() {
        var rows =[];
        this.props.trips.forEach(function(trip) {

            rows.push(<TripRow trip={trip} key={trip.id}   />);

        });


        return (<div className="TripTable">

                <table id="mainTable" className="center" onClick={(ev)=>{
                    var index =ev.target.parentElement.rowIndex;
                    if(index==undefined || index==0)
                        return
                    //sterg clasa selected din toate row urile
                    let allRows=document.getElementsByTagName("tr")
                    for (let i = 0; i < allRows.length; i++) {
                        allRows[i].className=allRows[i].className.replace(" selected","");
                    }

                    ev.target.parentElement.className+=" selected"
                    console.log("INDEX: ",index)
                    console.log("Element: ",this.props.trips[index-1])
                    TripForm.initFields(this.props.trips[index-1])


                }}>
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Place</th>
                        <th>Transport</th>
                        <th>Date</th>
                        <th>Price</th>
                        <th>NrTickets</th>
                        <th>Avaible Tickets</th>
                        <th>Delete</th>

                        <th></th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>

                </table>

            </div>
        );
    }
}
export default TripTable;