import axios from "axios";
import { API_BASE as SERVER_BASE } from "./http";
const API_BASE = `${SERVER_BASE}/cookie`;

function toFormData(cookie, imageFile) {
  const formData = new FormData();
  formData.append(
    "cookie",
    new Blob([JSON.stringify(cookie)], { type: "application/json" }),
  );
  if (imageFile) {
    formData.append("image", imageFile);
  }
  return formData;
}

export default {
  async getAll() {
    const response = await axios.get(`${API_BASE}/getAll`);
    return response.data;
  },

  async getAllForAdmin() {
    const response = await axios.get(`${API_BASE}/getAllForAdmin`);
    return response.data;
  },

  async save(cookie, imageFile) {
    return cookie.cookieId
      ? this.update(cookie, imageFile)
      : this.create(cookie, imageFile);
  },

  async create(cookie, imageFile) {
    const response = await axios.post(
      `${API_BASE}/create`,
      toFormData(cookie, imageFile),
    );
    return response.data;
  },

  async update(cookie, imageFile) {
    const response = await axios.put(
      `${API_BASE}/update/${cookie.cookieId}`,
      toFormData(cookie, imageFile),
    );
    return response.data;
  },

  async delete(cookieId) {
    await axios.delete(`${API_BASE}/delete/${cookieId}`);
  },

  async archive(cookieId) {
    const response = await axios.put(`${API_BASE}/archive/${cookieId}`);
    return response.data;
  },

  async unarchive(cookieId) {
    const response = await axios.put(`${API_BASE}/unarchive/${cookieId}`);
    return response.data;
  },

  async getById(cookieId) {
    const response = await axios.get(`${API_BASE}/read/${cookieId}`);
    return response.data;
  },

  getImageUrl(image) {
    return image ? `${SERVER_BASE}/uploads/${image}` : null;
  },
};
