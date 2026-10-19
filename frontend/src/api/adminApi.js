import axios from "axios";
import { API_BASE as SERVER_BASE } from "./http";

const API_BASE = `${SERVER_BASE}/admin`;

export default {
  async login(email, password) {
    const response = await axios.post(`${API_BASE}/login`, { email, password });
    return response.data;
  },

  async getAll() {
    const response = await axios.get(`${API_BASE}/getAll`);
    return response.data;
  },

  async create(admin) {
    const response = await axios.post(`${API_BASE}/create`, admin);
    return response.data;
  },

  async update(admin) {
    const response = await axios.put(`${API_BASE}/update`, admin);
    return response.data;
  },

  async delete(adminId) {
    await axios.delete(`${API_BASE}/delete/${adminId}`);
  },

  async setActive(adminId, active) {
    const response = await axios.put(`${API_BASE}/active/${adminId}`, null, {
      params: { active },
    });
    return response.data;
  },

  async resetPassword(adminId) {
    const response = await axios.post(`${API_BASE}/resetPassword/${adminId}`);
    return response.data;
  },

  async changePassword(adminId, currentPassword, newPassword) {
    const response = await axios.put(`${API_BASE}/changePassword/${adminId}`, {
      currentPassword,
      newPassword,
    });
    return response.data;
  },
};
