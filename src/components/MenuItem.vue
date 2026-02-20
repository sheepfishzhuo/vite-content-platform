<template>
  <el-sub-menu
    v-if="menu.children && menu.children.length > 0"
    :index="menu.path"
  >
    <template #title>
      <el-icon>
        <component :is="getIcon(menu.icon)" />
      </el-icon>
      <span>{{ menu.name }}</span>
    </template>
    <menu-item
      v-for="child in menu.children"
      :key="child.id"
      :menu="child"
    />
  </el-sub-menu>
  <el-menu-item
    v-else
    :index="menu.path"
    v-show="!menu.isHidden"
  >
    <el-icon>
      <component :is="getIcon(menu.icon)" />
    </el-icon>
    <template #title>{{ menu.name }}</template>
  </el-menu-item>
</template>

<script setup>
import { 
  HomeFilled, House, Document, Files, FolderOpened, Folder,
  EditPen, Edit, Delete, Plus, Check, Close, Setting, Tools, Menu, Grid, List, Operation,
  User, UserFilled, Avatar, Key, Lock, Bell, BellFilled, Message, ChatDotRound, Notification,
  Calendar, Clock, Timer, AlarmClock, Stopwatch, Location, MapLocation, Compass, Guide, Position,
  Picture, Camera, VideoCamera, Microphone, Headset, Monitor, Cellphone, Platform, OfficeBuilding, School, Shop,
  ShoppingCart, Goods, Box, Wallet, CreditCard, Coin, DataAnalysis, TrendCharts, DataLine, PieChart, Histogram, Odometer,
  Star, StarFilled, Medal, Trophy, Flag, Aim, Search, View, Hide, ZoomIn, ZoomOut, FullScreen,
  Upload, Download, Share, Link, Connection, Promotion, ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Back, Right,
  Top, Bottom, TopLeft, TopRight, BottomLeft, BottomRight, Refresh, RefreshLeft, RefreshRight,
  CircleCheck, CircleClose, CirclePlus, InfoFilled, Warning, WarningFilled, QuestionFilled, SuccessFilled,
  More, MoreFilled, Rank, Sort, Expand, Fold, Tickets, Phone
} from '@element-plus/icons-vue'

const iconMap = {
  HomeFilled, House, Document, Files, FolderOpened, Folder,
  EditPen, Edit, Delete, Plus, Check, Close, Setting, Tools, Menu, Grid, List, Operation,
  User, UserFilled, Avatar, Key, Lock, Bell, BellFilled, Message, ChatDotRound, Notification,
  Calendar, Clock, Timer, AlarmClock, Stopwatch, Location, MapLocation, Compass, Guide, Position,
  Picture, Camera, VideoCamera, Microphone, Headset, Monitor, Cellphone, Platform, OfficeBuilding, School, Shop,
  ShoppingCart, Goods, Box, Wallet, CreditCard, Coin, DataAnalysis, TrendCharts, DataLine, PieChart, Histogram, Odometer,
  Star, StarFilled, Medal, Trophy, Flag, Aim, Search, View, Hide, ZoomIn, ZoomOut, FullScreen,
  Upload, Download, Share, Link, Connection, Promotion, ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Back, Right,
  Top, Bottom, TopLeft, TopRight, BottomLeft, BottomRight, Refresh, RefreshLeft, RefreshRight,
  CircleCheck, CircleClose, CirclePlus, InfoFilled, Warning, WarningFilled, QuestionFilled, SuccessFilled,
  More, MoreFilled, Rank, Sort, Expand, Fold, Tickets, Phone
}

function getIcon(iconName) {
  return iconMap[iconName] || Document
}

defineProps({
  menu: {
    type: Object,
    required: true
  }
})
</script>
