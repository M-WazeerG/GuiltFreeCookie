import axios from "axios";
import { API_BASE } from "./http";

export default {
  async subscribe(email) {
    await axios.post(`${API_BASE}/newsletter/subscribe`, { email });
  },
};
