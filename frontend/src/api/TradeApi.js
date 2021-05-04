import axios from "axios";

const pre = `http://localhost:8080/api`;


class TradeApi {

    static getAllTrades() {
        // let token = jwtDecode(localStorage.getItem('jwtToken'))
        // const userid = token ? token.id : 1;

        const userid = 1;


        return axios.get(pre + `/Trades/investor/${userid}`).then(res => res.data);
    }

    static saveTrade(trade) {
            if (trade.id) {
                return axios.post(pre + `/Trades/update/${trade.id}`, trade).then(res => res.data);
            } else {
                return axios.post(pre + `/Trades/create`, trade).then(res => res.data);
            }
    }

    static deleteTrade(id) {
        return axios.delete(pre + `/Trades/delete/${id}`)
    }


    static getTrade(id) {
        return axios.get(`http://192.168.1.3:8080/api/Trades/${id}`).then(res => res.data)
    }

}

export default TradeApi;
