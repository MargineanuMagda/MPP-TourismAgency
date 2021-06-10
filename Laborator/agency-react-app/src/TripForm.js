import React from  'react';
class TripForm extends React.Component {
    constructor(props) {
        super(props);
        this.state={id: '',place: '',transport: '',date: '',price: '',nrTickets: '',freeTickets: ''}
    }

    static initFields(trip){
        console.log("init trip fields")

        this.state={id: trip.id,place: trip.place,transport: trip.transport,date: trip.date,price: trip.price,nrTickets: trip.nrTickets,freeTickets: trip.freeTickets}
        document.getElementById("idLbl").value=trip.id;
        document.getElementById("place").value=trip.place;
        document.getElementById("transport").value=trip.transport;
        console.log(trip.date)
        var dateTime=trip.date.split(" ")
        document.getElementById("date").value=dateTime[0];

        document.getElementById("time").value=dateTime[1];
        document.getElementById("price").value=trip.price;
        document.getElementById("tick").value=trip.nrTickets;
        document.getElementById("freeTick").value=trip.freeTickets;
    }

render() {
    return (

        <form onSubmit={this.handleSubmit}>
            <label id="idLbl" value="0"><br/></label>
            <label>Place<input id="place"/><br/></label>

            <label>Transport<input id="transport" type="text"/><br/></label>
            <label>Date<input id="date" type="date"/></label>
            <label>Time<input id="time" type="time"/><br/></label>
            <label>Price<input id="price" type="number"/><br/></label>
            <label>Nr Tickets<input id="tick" type="number"/><br/></label>
            <label>Avaible Tickets<input id="freeTick" type="number"/><br/></label>
            <input type="button" value="Clear" onClick={this.clearFields}/>
            <input type="submit" value="Add"/>
            <input type="button" value="Update" onClick={this.updateTrips}/>
            <input type="button" value="Delete" onClick={this.deleteTrips}/>
        </form>
    )


}



    handleSubmit=(event)=> {
        console.log(document.getElementById("idLbl").value)
        this.setState({
            id: document.getElementById("idLbl").value,
            place: document.getElementById("place").value,
            transport: document.getElementById("transport").value,
            date: document.getElementById("date").value+" "+document.getElementById("time").value,
            price: document.getElementById("price").value,
            nrTickets: document.getElementById("tick").value,
            freeTickets: document.getElementById("freeTick").value
        })


        var trip={id:document.getElementById("idLbl").value,
            place: document.getElementById("place").value,
            transport: document.getElementById("transport").value,
            date: document.getElementById("date").value+" "+document.getElementById("time").value,
            price: document.getElementById("price").value,
            nrTickets: document.getElementById("tick").value,
            freeTickets: document.getElementById("freeTick").value
        }
        console.log("Trip was submitted: ");
        console.log(trip);
        this.props.addFunc(trip);
        event.preventDefault();
        this.clearFields(event);
    }

    clearFields=(event)=>  {
        this.setState({id: '0',place: '',transport: '',date: '',price: '',nrTickets: '',freeTickets: ''});
        document.getElementById("idLbl").value='0';
        document.getElementById("place").value="";
        document.getElementById("transport").value="";
        document.getElementById("date").value="";

        document.getElementById("time").value="";
        document.getElementById("price").value="";
        document.getElementById("tick").value="";
        document.getElementById("freeTick").value="";


    }

    updateTrips=(event)=> {
        this.setState({
            id: document.getElementById("idLbl").value,
            place: document.getElementById("place").value,
            transport: document.getElementById("transport").value,
            date: document.getElementById("date").value+" "+document.getElementById("time").value,
            price: document.getElementById("price").value,
            nrTickets: document.getElementById("tick").value,
            freeTickets: document.getElementById("freeTick").value
        })


        var trip={id:document.getElementById("idLbl").value,
            place: document.getElementById("place").value,
            transport: document.getElementById("transport").value,
            date: document.getElementById("date").value+" "+document.getElementById("time").value,
            price: document.getElementById("price").value,
            nrTickets: document.getElementById("tick").value,
            freeTickets: document.getElementById("freeTick").value
        }
        console.log("Trip was updated: ");
        console.log(trip);
        this.props.updateFunc(trip);
        event.preventDefault();
        this.clearFields(event);
    }

    deleteTrips=(event)=>  {
        this.setState({
            id: document.getElementById("idLbl").value,
            place: document.getElementById("place").value,
            transport: document.getElementById("transport").value,
            date: document.getElementById("date").value+" "+document.getElementById("time").value,
            price: document.getElementById("price").value,
            nrTickets: document.getElementById("tick").value,
            freeTickets: document.getElementById("freeTick").value
        })


        var id=document.getElementById("idLbl").value;
        console.log("Trip id to delete: ");
        console.log(id);
        this.props.deleteFunc(id);
        event.preventDefault();
        this.clearFields(event);
    }
}
export default TripForm;