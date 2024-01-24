import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import MyPageView from '@/views/MyPageView.vue'
import MyPage from '@/components/mypage/MyPage.vue'
import MeetingView from '@/views/MeetingView.vue'
import ResultPage from '@/views/ResultView.vue'
import MyDetail from '@/components/mypage/MyDetail.vue'
import MyAccount from '@/components/mypage/MyAccount.vue'

// import MyMeeting from '@/components/conference/MyMeeting.vue'
// import MeetingView from '@/views/MeetingView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      // path: '/${userId}',
      path: '/mypage',
      name: 'mypage',
      component: MyPageView,
      children: [
        {
          path: '',
          name: MyPage,
          component: MyPage
        },
        {
          path: 'detail',
          name: 'mydetail',
          component: MyDetail
        },
        {
          path: 'account',
          name: 'account',
          component: MyAccount
        }
      ]
    },
    {
      path: '/meeting/:accessType',
      name: 'meeting',
      component: MeetingView,
      props: true
    },
    {
      path: '/resultpage',
      name: 'resultpage',
      component: ResultPage
    }
  ]
})

export default router
