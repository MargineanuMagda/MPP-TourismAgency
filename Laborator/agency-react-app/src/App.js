
import './App.css';
import React from  'react';
import TripForm from "./TripForm"
import TripTable from "./TripTable"
import {GetTrips,AddTrip,DeleteTrip,UpdateTrip} from  './utils/rest-utils'

class App extends React.Component{
    constructor(props){
        super(props);
        this.state={trips:[{}],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
            updateFunc:this.updateFunc.bind(this),
        }
        console.log('UserApp constructor')


    }

    addFunc(trip){
        console.log('inside add Func '+trip);
        AddTrip(trip)
            .then(res=> GetTrips())
            .then(trips=>this.setState({trips}))
            .catch(erorr=>console.log('eroare add ',erorr));
    }
    updateFunc(trip){
        console.log('inside update Func '+trip);
        UpdateTrip(trip)
            .then(res=> GetTrips())
            .then(trips=>this.setState({trips}))
            .catch(erorr=>console.log('eroare update ',erorr));
    }
    deleteFunc(trip){
        console.log('inside deleteFunc '+trip);
        DeleteTrip(trip)
            .then(res=> GetTrips())
            .then(trips=>this.setState({trips}))
            .catch(error=>console.log('eroare delete', error));
    }
    componentDidMount(){
        console.log('inside componentDidMount')
        GetTrips().then(trips=>this.setState({trips}));

    }
  render(){
    return(
        <div className="TripApp">
          <h1>Agency trips Management</h1>
          <TripForm addFunc={this.state.addFunc} updateFunc={this.state.updateFunc} deleteFunc={this.state.deleteFunc}/>
          <br/>
          <br/>
          <TripTable trips={this.state.trips} />
        </div>
    );
  }
}
export default App;
