import {CHAT_USERS_BASE_URL} from './const';

function status(response) {
    console.log('response status '+response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}
export function GetTrips(){
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    var myInit = { method: 'GET',
        headers: headers,
        mode: 'cors'};
    var request = new Request(CHAT_USERS_BASE_URL, myInit);

    console.log('Inainte de fetch pentru '+CHAT_USERS_BASE_URL)

    return fetch(request)
        .then(status)
        .then(json)
        .then(data=> {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error=>{
            console.log('Request failed', error);
            return error;
        });

}
export function AddTrip(trip){
    console.log('trips is '+trip)
    console.log('inainte de fetch post'+JSON.stringify(trip));

    var myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type","application/json");

    var antet = { method: 'POST',
        headers: myHeaders,
        mode: 'cors',
        body:JSON.stringify(trip)};

    return fetch(CHAT_USERS_BASE_URL,antet)
        .then(status)
        .then(response=>{
            return response.text();
        }).catch(error=>{
            console.log('Request failed', error);
            return Promise.reject(error);
        }); //;


}
export function UpdateTrip(trip){
    console.log('trips is '+trip)
    console.log('inainte de fetch post'+JSON.stringify(trip));

    var myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type","application/json");

    var antet = { method: 'PUT',
        headers: myHeaders,
        mode: 'cors',
        body:JSON.stringify(trip)};

    return fetch(CHAT_USERS_BASE_URL+trip.id,antet)
        .then(status)
        .then(response=>{
            return response.text();
        }).catch(error=>{
            console.log('Request failed', error);
            return Promise.reject(error);
        }); //;


}
export function DeleteTrip(username){
    console.log('inainte de fetch delete')
    var myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");

    var antet = { method: 'DELETE',
        headers: myHeaders,
        mode: 'cors'};

    var userDelUrl=CHAT_USERS_BASE_URL+username;

    return fetch(userDelUrl,antet)
        .then(status)
        .then(response=>{
            console.log('Delete status '+response.status);
            return response.text();
        }).catch(e=>{
            console.log('error '+e);
            return Promise.reject(e);
        });

}

