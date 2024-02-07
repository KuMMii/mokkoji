import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const { VITE_API_URL } = import.meta.env
const { VITE_SERVER } = import.meta.env

export const useSessionStore = defineStore('session', () => {
  const router = useRouter()

  const createSession = () => {
    router.push('/meetings')
    sessionStorage.setItem('sessionId', '')
    sessionStorage.setItem('isHost', true)
  }

  const findSession = async (sessionId) => {
    try {
      const res = await axios.get(VITE_API_URL + VITE_SERVER + `/meetings/sessions/${sessionId}`)
      if (res.data.sessionId !== undefined) {
        sessionStorage.setItem('sessionId', res.data.sessionId)
        sessionStorage.setItem('isHost', false)
        return 'success'
      } else {
        return 'fail'
      }
    } catch (error) {
      console.error(error)
    }
  }

  const deleteSession = async (sessionId) => {
    try {
      const res = await axios.delete(VITE_API_URL + VITE_SERVER + `/meetings/sessions/${sessionId}`)
    } catch (error) {
      console.error(error)
    }
  }

  return {
    createSession,
    findSession,
    deleteSession
  }
})

export const useLetterStore = defineStore('letter', () => {
  const sendLetter = async (audioFile, videoFile, sessionId) => {
    // FormData 객체 생성
    const formData = new FormData()

    console.log(sessionId)

    // FormData에 음성 파일 추가
    if (audioFile !== null || audioFile !== undefined) formData.append('audio', audioFile)

    // FormData에 영상 파일 추가
    if (videoFile !== null || videoFile !== undefined) formData.append('video', videoFile)

    try {
      // axios를 사용하여 POST 요청 보내기
      const response = await axios.post(
        VITE_API_URL + VITE_SERVER + `/events/rollingpapers/${sessionId}`,
        formData,
        {
          // 필수: FormData를 사용할 때는 이 헤더를 설정해야 함
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      // 성공 시 서버의 응답을 처리
      console.log('서버 응답:', response.data)
    } catch (error) {
      // 오류 처리
      console.error('오류 발생:', error)
    }
  }

  return {
    sendLetter
  }
})